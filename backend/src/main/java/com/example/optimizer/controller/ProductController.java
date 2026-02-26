package com.example.optimizer.controller;

import com.example.optimizer.dto.ProductRequest;
import com.example.optimizer.dto.ProductResponse;
import com.example.optimizer.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductService service;

    @GET
    public List<ProductResponse> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    public ProductResponse getById(@PathParam("id") UUID id) {
        return service.findById(id);
    }

    @POST
    public Response create(@Valid ProductRequest request) {
        ProductResponse created = service.create(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public ProductResponse update(@PathParam("id") UUID id, @Valid ProductRequest request) {
        return service.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
