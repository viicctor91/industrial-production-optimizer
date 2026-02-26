package com.example.optimizer.controller;

import com.example.optimizer.dto.RawMaterialRequest;
import com.example.optimizer.dto.RawMaterialResponse;
import com.example.optimizer.service.RawMaterialService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/api/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialController {

    @Inject
    RawMaterialService service;

    @GET
    public List<RawMaterialResponse> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    public RawMaterialResponse getById(@PathParam("id") UUID id) {
        return service.findById(id);
    }

    @POST
    public Response create(@Valid RawMaterialRequest request) {
        RawMaterialResponse created = service.create(request);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public RawMaterialResponse update(@PathParam("id") UUID id, @Valid RawMaterialRequest request) {
        return service.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
