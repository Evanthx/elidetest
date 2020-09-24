// Copyright (C) 2014 Federico Recio
/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.usermind.elidetest.swagger;


import com.usermind.elidetest.dropwizard.WorkerConfiguration;
import io.dropwizard.views.View;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;


/**
 * Serves the content of Swagger's index page which has been "templatized" to
 * support replacing the directory in which Swagger's static content is located
 * (i.e. JS files) and the path with which requests to resources need to be
 * prefixed.
 */
public class SwaggerView extends View {

    //Sonar says this should be configurable, but it will break things if it changes!
    private static final String SWAGGER_URI_PATH = "/swagger-static"; //NOSONAR

    private final String swaggerAssetsPath;
    private final String contextPath;

    private final WorkerConfiguration config;

    public SwaggerView(@Nonnull final String urlPattern,
                       @Nonnull WorkerConfiguration config) {
        super(config.getTemplateUrl(), StandardCharsets.UTF_8);

        if ("/".equals(urlPattern)) {
            swaggerAssetsPath = SWAGGER_URI_PATH;
        } else {
            swaggerAssetsPath = urlPattern + SWAGGER_URI_PATH;
        }

        if ("/".equals(urlPattern)) {
            contextPath = "";
        } else {
            contextPath = urlPattern;
        }

        this.config = config;
    }

    /**
     * Returns the title for the browser header
     */
    public String getTitle() {
        return config.getPageTitle();
    }

    /**
     * Returns the path with which all requests for Swagger's static content
     * need to be prefixed
     */
    public String getSwaggerAssetsPath() {
        return swaggerAssetsPath;
    }

    /**
     * Returns the path with with which all requests made by Swagger's UI to
     * Resources need to be prefixed
     */
    public String getContextPath() {
        return contextPath;
    }

}
