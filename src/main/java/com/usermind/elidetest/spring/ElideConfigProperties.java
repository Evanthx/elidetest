/*
 * Copyright 2019, Yahoo Inc.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package com.usermind.elidetest.spring;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Configuration settings for Elide.
 */
@Data
@Component
public class ElideConfigProperties {

    /**
     * Settings for the JSON-API controller.
     */
    private ControllerProperties jsonApi = new ControllerProperties();

    /**
     * Settings for the GraphQL controller.
     */
    private ControllerProperties graphql = new ControllerProperties();

    /**
     * Settings for the Swagger document controller.
     */
    private SwaggerControllerProperties swagger = new SwaggerControllerProperties();

    /**
     * Default pagination size for collections if the client doesn't paginate.
     */
    private int pageSize = 500;

    /**
     * The maximum pagination size a client can request.
     */
    private int maxPageSize = 10000;
}
