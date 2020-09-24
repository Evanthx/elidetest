package com.usermind.elidetest.spring;

import com.usermind.elidetest.models.ArtifactGroup;
import com.usermind.elidetest.models.ArtifactProduct;
import com.usermind.elidetest.models.ArtifactVersion;
import com.yahoo.elide.Elide;
import com.yahoo.elide.ElideSettingsBuilder;
import com.yahoo.elide.Injector;
import com.yahoo.elide.audit.Slf4jLogger;
import com.yahoo.elide.contrib.swagger.SwaggerBuilder;
import com.yahoo.elide.core.DataStore;
import com.yahoo.elide.core.EntityDictionary;
import com.yahoo.elide.core.filter.dialect.RSQLFilterDialect;
import com.yahoo.elide.datastores.jpa.JpaDataStore;
import com.yahoo.elide.datastores.jpa.transaction.NonJtaTransaction;
import com.yahoo.elide.datastores.noop.NoopDataStore;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceProvider;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.TimeZone;
import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = { "com.usermind" })
public class SpringConfiguration {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory httpClientFactory) {
        return new RestTemplate(httpClientFactory);
        //If you need to add message converters to RestTemplate, this is a good place to do it.
        //Similarly you can make it return an error instead of throwing an exception here if desired.
    }

    /**
     * Provide an Apache HTTP Components HTTP Client
     */
    @Bean
    public ClientHttpRequestFactory httpClientFactory() {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        return new PreemptiveDigestAuthenticationRequestFactory(httpclient);
    }

    /**
     * Creates the Elide instance with standard settings.
     * @param dictionary Stores the static metadata about Elide models.
     * @param dataStore The persistence store.
     * @param settings Elide settings.
     * @return A new elide instance.
     */
    @Bean(name = "elide")
    public Elide initializeElide(EntityDictionary dictionary,
                                 DataStore dataStore, ElideConfigProperties settings) {

        ElideSettingsBuilder builder = new ElideSettingsBuilder(dataStore)
                .withEntityDictionary(dictionary)
                .withDefaultMaxPageSize(settings.getMaxPageSize())
                .withDefaultPageSize(settings.getPageSize())
                .withUseFilterExpressions(true)
                .withJoinFilterDialect(new RSQLFilterDialect(dictionary))
                .withSubqueryFilterDialect(new RSQLFilterDialect(dictionary))
                .withAuditLogger(new Slf4jLogger())
                .withEncodeErrorResponses(true)
                .withISO8601Dates("yyyy-MM-dd'T'HH:mm'Z'", TimeZone.getTimeZone("UTC"));

        return new Elide(builder.build());
    }

    @Bean
    public EntityDictionary buildDictionary(AutowireCapableBeanFactory beanFactory) {
        EntityDictionary dictionary = new EntityDictionary(new HashMap());

        dictionary.bindEntity(ArtifactGroup.class);
        dictionary.bindEntity(ArtifactProduct.class);
        dictionary.bindEntity(ArtifactVersion.class);


//        EntityDictionary dictionary = new EntityDictionary(new HashMap<>(),
//                new Injector() {
//                    @Override
//                    public void inject(Object entity) {
//                        beanFactory.autowireBean(entity);
//                    }
//
//                    @Override
//                    public <T> T instantiate(Class<T> cls) {
//                        return beanFactory.createBean(cls);
//                    }
//                });
//
        dictionary.scanForSecurityChecks();
        return dictionary;
    }

    /**
     * Creates the DataStore Elide.  Override to use a different store.
     * @param entityManagerFactory The JPA factory which creates entity managers.
     * @return An instance of a JPA DataStore.
     */
    @Bean
    public DataStore buildDataStore()  {
        return new NoopDataStore(Arrays.asList(NoopBean.class));
    }

    @Bean
    public Swagger buildSwagger(EntityDictionary dictionary, ElideConfigProperties settings) {
        Info info = new Info()
                .title(settings.getSwagger().getName())
                .version(settings.getSwagger().getVersion());

        SwaggerBuilder builder = new SwaggerBuilder(dictionary, info).withLegacyFilterDialect(false);

        Swagger swagger = builder.build().basePath(settings.getJsonApi().getPath());

        return swagger;
    }


/*

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

        return properties;
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/spring_jpa");
        dataSource.setUsername( "tutorialuser" );
        dataSource.setPassword( "tutorialmy5ql" );
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "com.baeldung.persistence.model" });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public EntityManagerFactory createEntityManagerFactory() {
//       return Persistence.createEntityManagerFactory("elide");

        PersistenceProvider provider = new HibernatePersistenceProvider();
        return provider.createEntityManagerFactory("elide", new HashMap<Object, Object>());
//        EntityManagerFactory emf = provider.createEntityManagerFactory("elide", new HashMap<>());
//        return emf;
    }
    /*
    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
      **factory.setPersistenceProvider(new HibernatePersistenceProvider());**
        factory.setPackagesToScan("com.company.appname.persistence.domain");
        factory.setDataSource(dataSource());

        factory.setJpaProperties(hibernateProperties());
        factory.afterPropertiesSet();

        return factory.getObject();
    }
    */


}
