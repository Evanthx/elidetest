package com.usermind.elidetest.dropwizard;

import io.dropwizard.Configuration;

public class DropWizardConfiguration extends Configuration {

    private WorkerConfiguration workerConfiguration = new WorkerConfiguration();
    private DropwizardElideConfiguration elideConfiguration = new DropwizardElideConfiguration();


    public WorkerConfiguration getWorkerConfiguration() {
        return workerConfiguration;
    }

    public DropwizardElideConfiguration getElideConfiguration() {
        return elideConfiguration;
    }
}
