package com.usermind.elidetest.dropwizard.urlhandlers;

import io.dropwizard.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RootView extends View {

    @Autowired
    public RootView() {
        super("/views/ftl/rootTemplate.ftl");
    }

    public long getThreads() {
        return Thread.activeCount();
    }
}
