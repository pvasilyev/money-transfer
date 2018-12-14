package com.money.transfer.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This exception-mapper might come in handy whenever server can't respond gracefully and there is need to respond
 * with non-HTTP-200 OK status, but rather with HTTP-500 (Internal Server Error).
 *
 * @author pvasilyev
 */
@Provider
public class UncaughtException extends Throwable implements ExceptionMapper<Throwable> {

    private static final long serialVersionUID = 1L;

    @Override
    public Response toResponse(Throwable exception) {
        return Response.status(500)
                .entity("Something bad happened. Please try again later. " + exception.getMessage())
                .type("text/plain")
                .build();
    }
}