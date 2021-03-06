/*
 * Copyright 2019, Verizon Media.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */

package com.usermind.elidetest.dropwizard;

import com.google.common.collect.Lists;
import com.usermind.elidetest.AppMain;
import com.usermind.elidetest.models.ArtifactGroup;
import com.usermind.elidetest.models.ArtifactProduct;
import com.usermind.elidetest.models.ArtifactVersion;
import com.yahoo.elide.contrib.swagger.SwaggerBuilder;
import com.yahoo.elide.core.EntityDictionary;
import com.yahoo.elide.standalone.config.ElideStandaloneSettings;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.IOException;
import java.util.*;

/**
 * This class contains common settings for both test and production.
 */
public abstract class Settings implements ElideStandaloneSettings {

    protected String jdbcUrl;
    protected String jdbcUser;
    protected String jdbcPassword;

    protected boolean inMemory;

    public Settings(boolean inMemory) {
        jdbcUrl = Optional.ofNullable(System.getenv("JDBC_DATABASE_URL"))
                .orElse("jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1");

        jdbcUser = Optional.ofNullable(System.getenv("JDBC_DATABASE_USERNAME"))
                .orElse("sa");

        jdbcPassword = Optional.ofNullable(System.getenv("JDBC_DATABASE_PASSWORD"))
                .orElse("");

        this.inMemory = inMemory;
    }

    @Override
    public int getPort() {
        //Heroku exports port to come from $PORT
        return Optional.ofNullable(System.getenv("PORT"))
                .map(Integer::valueOf)
                .orElse(8080);
    }

    @Override
    public Map<String, Swagger> enableSwagger() {
        EntityDictionary dictionary = new EntityDictionary(new HashMap());

        dictionary.bindEntity(ArtifactGroup.class);
        dictionary.bindEntity(ArtifactProduct.class);
        dictionary.bindEntity(ArtifactVersion.class);
        Info info = new Info().title("Test Service").version("1.0");

        SwaggerBuilder builder = new SwaggerBuilder(dictionary, info);

        Swagger swagger = builder.build().basePath("/api/v1");

        Map<String, Swagger> docs = new HashMap<>();
        docs.put("test", swagger);
        return docs;
    }

    @Override
    public String getModelPackageName() {

        //This needs to be changed to the package where your models live.
        return "example.models";
    }

    @Override
    public Properties getDatabaseProperties() {
        Properties dbProps;

        if (inMemory) {
            return getInMemoryProps();
        }

        try {
            dbProps = new Properties();
            dbProps.load(
                    AppMain.class.getClassLoader().getResourceAsStream("dbconfig.properties")
            );

            dbProps.setProperty("javax.persistence.jdbc.url", jdbcUrl);
            dbProps.setProperty("javax.persistence.jdbc.user", jdbcUser);
            dbProps.setProperty("javax.persistence.jdbc.password", jdbcPassword);
            return dbProps;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Class<?>> getFilters() {
        return Lists.newArrayList(CorsFilter.class);
    }

    @Override
    public void updateServletContextHandler(ServletContextHandler servletContextHandler) {
       ResourceHandler resource_handler = new ResourceHandler();

       try {
           resource_handler.setDirectoriesListed(false);
           resource_handler.setResourceBase(Settings.class.getClassLoader()
                   .getResource("META-INF/resources/").toURI().toString());
           servletContextHandler.insertHandler(resource_handler);
       } catch (Exception e) {
           throw new IllegalStateException(e);
       }
    }

    protected Properties getInMemoryProps() {
        Properties options = new Properties();

        options.put("hibernate.show_sql", "true");
        options.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        options.put("hibernate.current_session_context_class", "thread");
        options.put("hibernate.jdbc.use_scrollable_resultset", "true");
        options.put("hibernate.default_batch_fetch_size", 100);

        options.put("javax.persistence.jdbc.driver", "org.h2.Driver");
        options.put("javax.persistence.jdbc.url", jdbcUrl);
        options.put("javax.persistence.jdbc.user", jdbcUser);
        options.put("javax.persistence.jdbc.password", jdbcPassword);

        return options;
    }
}
