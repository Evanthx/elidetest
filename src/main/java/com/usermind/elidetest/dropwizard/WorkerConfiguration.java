package com.usermind.elidetest.dropwizard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import io.dropwizard.Configuration;
import io.swagger.jaxrs.config.BeanConfig;
import org.hibernate.validator.constraints.NotEmpty;
public class WorkerConfiguration extends Configuration {

    private static final String DEFAULT_TITLE = "Usermindsdk project";
    private static final String DEFAULT_TEMPLATE = "index.ftl";

    //The test field comes from the config.yml file.
    String test = "";

    //Swagger starts//
    private String pageTitle;
    private String templateUrl;

    @NotEmpty
    private String resourcePackage;

    private String title;
    private String version;
    private String description;
    private Boolean prettyPrint = true;
    private String host;
    private String[] schemes = new String[]{"http"};
    private Boolean enabled = true;

    @JsonProperty
    private String uriPrefix = "";

    //Swagger ends//


    public WorkerConfiguration() {
        this.pageTitle = DEFAULT_TITLE;
        this.templateUrl = DEFAULT_TEMPLATE;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    //Swagger starts//
    @JsonProperty
    public String getPageTitle() {
        return pageTitle;
    }

    @JsonProperty
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @JsonProperty
    public String getTemplateUrl() {
        return templateUrl;
    }

    @JsonProperty
    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    @JsonProperty
    public String getResourcePackage() {
        return resourcePackage;
    }

    @JsonProperty
    public void setResourcePackage(String resourcePackage) {
        this.resourcePackage = resourcePackage;
    }

    @JsonProperty
    public String getTitle() {
        return title;
    }

    @JsonProperty
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty
    public String getVersion() {
        return version;
    }

    @JsonProperty
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty
    public Boolean getPrettyPrint() {
        return prettyPrint;
    }

    @JsonProperty
    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    @JsonProperty
    public String getHost() {
        return host;
    }

    @JsonProperty
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty
    public String[] getSchemes() {
        return schemes;
    }

    @JsonProperty
    public void setSchemes(String[] schemes) {
        this.schemes = schemes;
    }

    @JsonProperty
    public Boolean isEnabled() {
        return enabled;
    }

    @JsonProperty
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty
    public String getUriPrefix() {
        return uriPrefix;
    }

    @JsonProperty
    public void setUriPrefix(String uriPrefix) {
        this.uriPrefix = uriPrefix;
    }

    @JsonIgnore
    public BeanConfig build(String urlPattern) {
        if (Strings.isNullOrEmpty(resourcePackage)) {
            throw new IllegalStateException(
                    "Resource package needs to be specified"
                            + " for Swagger to correctly detect annotated resources");
        }

        final BeanConfig config = new BeanConfig();
        config.setTitle(title);
        config.setVersion(version);
        config.setDescription(description);
        config.setPrettyPrint(prettyPrint);
        config.setBasePath(urlPattern);
        config.setResourcePackage(resourcePackage);
        config.setSchemes(schemes);
        config.setHost(host);
        config.setScan(true);
        return config;
    }
    //Swagger ends//

}



