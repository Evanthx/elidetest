package com.usermind.elidetest.dropwizard;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class NoSuchMethodExceptionMapper implements ExceptionMapper<NoSuchMethodException> {
    public Response toResponse(NoSuchMethodException exception) {
        return Response.status(501)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}

