package com.usermind.elidetest.healthchecks;

import com.codahale.metrics.health.HealthCheck;

public class AppRunningHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
