package com.usermind.elidetest.dropwizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.usermind.elidetest.healthchecks.AppRunningHealthCheck;
import com.usermind.elidetest.helpers.JsonSerialization;
import com.usermind.elidetest.models.ArtifactGroup;
import com.usermind.elidetest.models.ArtifactProduct;
import com.usermind.elidetest.models.ArtifactVersion;
import com.usermind.elidetest.spring.SpringConfiguration;
import com.usermind.elidetest.swagger.SwaggerBundle;
import com.yahoo.elide.Elide;
import com.yahoo.elide.resources.JsonApiEndpoint;
import com.yahoo.elide.standalone.ElideStandalone;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.Path;
import java.util.Map;
import com.yahoo.elide.standalone.ElideStandalone;
import com.yahoo.elide.standalone.config.ElideStandaloneSettings;

@Configuration
public class DropWizardService extends Application<DropWizardConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropWizardService.class);
    private ObjectMapper objectMapper = null;
    public static final String GENERAL_ERROR_PAGE = "/error/general-error";

    //Sonar wants this to be protected, but it needs to be public for testbase to set itself up properly
    public static final Class[] SPRING_CONFIG_CLASSES = new Class[]{  //NOSONAR
            SpringConfiguration.class
    };

    @Override
    public void initialize(Bootstrap<DropWizardConfiguration> bootstrap) {
        LOGGER.info("Init Jetty and Dropwizard");
        objectMapper =  bootstrap.getObjectMapper();
        if (objectMapper == null) {
            objectMapper = Jackson.newObjectMapper();
        }

        bootstrap.addBundle(new SwaggerBundle<DropWizardConfiguration>() {
            @Override
            protected WorkerConfiguration getSwaggerBundleConfiguration(DropWizardConfiguration configuration) {
                return configuration.getWorkerConfiguration();
            }
        });

        bootstrap.addBundle(new ViewBundle());
        JsonSerialization.configure(objectMapper);
/*
        ElideBundle<DropWizardConfiguration> elideBundle = new ElideBundle<DropWizardConfiguration>(
                ArtifactGroup.class,
                ArtifactProduct.class,
                ArtifactVersion.class
        ) {
            @Override
            public DataSourceFactory getDataSourceFactory(DropWizardConfiguration configuration) {
                return configuration.getElideConfiguration().getDataSourceFactory();
            }
        };



        bootstrap.addBundle(elideBundle);
        */

    }

    @Override
    public void run(DropWizardConfiguration config, Environment environment) {
        LOGGER.info("Run Server");

        environment.jersey().register(RolesAllowedDynamicFeature.class);

        //Set up error handlers
        environment.jersey().register(new GenericExceptionMapper());
        environment.jersey().register(new NoSuchMethodExceptionMapper());

        final ErrorPageErrorHandler epeh = new ErrorPageErrorHandler();
        // 400 - Bad Request, leave alone
        epeh.addErrorPage(401, GENERAL_ERROR_PAGE);
        epeh.addErrorPage(402, GENERAL_ERROR_PAGE);
        epeh.addErrorPage(403, "/error/403");
        epeh.addErrorPage(404, "/error/404");
        epeh.addErrorPage(405, 499, GENERAL_ERROR_PAGE);
        epeh.addErrorPage(500, 599, GENERAL_ERROR_PAGE);
        environment.getApplicationContext().setErrorHandler(epeh);


        environment.healthChecks().register("AppRunningHealthCheck", new AppRunningHealthCheck());

        //before we init the app context, create a parent context with any DW config objects to provide the child context
        final AnnotationConfigApplicationContext dwAppContext = new AnnotationConfigApplicationContext();
        dwAppContext.refresh();
        // provide dropwizard objects for dependency injection
        dwAppContext.getBeanFactory().registerSingleton("dwConfiguration", config);
        dwAppContext.getBeanFactory().registerSingleton("appConfig", config.getWorkerConfiguration());
        dwAppContext.getBeanFactory().registerSingleton("dwEnvironment", environment);
        dwAppContext.getBeanFactory().registerSingleton("dwObjectMapperFactory", objectMapper);
        dwAppContext.registerShutdownHook();
        dwAppContext.start();

//        Settings settings = new Settings(true) {};
//        ElideStandalone elide = new ElideStandalone(settings);
//
//        try {
//            elide.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // the primary Spring ApplicationContext
        initSpring(dwAppContext, environment);

        //testing
        final ImmutableList.Builder<Class<?>> builder = ImmutableList.builder();
        for (Object o : environment.jersey().getResourceConfig().getSingletons()) {
            if (o.getClass().isAnnotationPresent(Path.class)) {
                builder.add(o.getClass());
            }
        }
        for (Class<?> klass : environment.jersey().getResourceConfig().getClasses()) {
            if (klass.isAnnotationPresent(Path.class)) {
                builder.add(klass);
            }
        }

        return;
    }

    private ApplicationContext initSpring(ApplicationContext parent, Environment environment) {
        //the real main app context has a link to the parent context
        final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.setParent(parent);
        ctx.register(SPRING_CONFIG_CLASSES);
        ctx.refresh();
        ctx.registerShutdownHook();
        ctx.start();

        // Jersey
        Map<String, Object> resources = ctx.getBeansWithAnnotation(Path.class);
        for(Map.Entry<String,Object> entry : resources.entrySet()) {
            environment.jersey().register(entry.getValue());
        }

        //Register Elide
        JsonApiEndpoint jsonApiEndpoint = new JsonApiEndpoint(ctx.getBean(Elide.class), null);
        environment.jersey().register(jsonApiEndpoint);
//        environment.jersey().register(JsonApiEndpoint.class);

        // Managed
        Map<String, Managed> managedMap = ctx.getBeansOfType(Managed.class);
        for(Map.Entry<String,Managed> entry : managedMap.entrySet()) {
            /* These are being managed here so that the thread or other resources can be
                started and stopped by DropWizard.
             */
            environment.lifecycle().manage(entry.getValue());
        }

        return ctx;
    }
}
