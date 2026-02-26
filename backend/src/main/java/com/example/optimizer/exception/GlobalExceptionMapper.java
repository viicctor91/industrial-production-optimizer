package com.example.optimizer.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        int status = 500;
        String error = "Internal Server Error";
        if (ex instanceof ResourceNotFoundException) {
            status = 404;
            error = "Not Found";
        } else if (ex instanceof DuplicateResourceException || ex instanceof IllegalArgumentException) {
            status = 400;
            error = "Bad Request";
        }
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status);
        body.put("error", error);
        body.put("message", ex.getMessage());
        body.put("path", ""); // Quarkus filters don't provide request path here easily
        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(body).build();
    }
}
