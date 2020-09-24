package com.usermind.elidetest.dropwizard.urlhandlers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/")
@Api(value = "Root Page Handler")
@Produces(MediaType.TEXT_HTML)
public class RootURLHandler {

    private final RootView rootView;

    @Autowired
    public RootURLHandler(RootView rootView) {
        this.rootView = rootView;
    }

    @GET
    @ApiOperation(value = "Home Page",
            notes = "Add notes for swagger.",
            tags = "internal")
    public RootView showRootPage() {
        return rootView;
    }

}
