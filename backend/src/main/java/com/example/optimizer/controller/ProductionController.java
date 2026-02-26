package com.example.optimizer.controller;

import com.example.optimizer.dto.ProductionSuggestionResponse;
import com.example.optimizer.service.ProductionPlannerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/production")
@Produces(MediaType.APPLICATION_JSON)
public class ProductionController {

    @Inject
    ProductionPlannerService service;

    @POST
    @Path("/suggest")
    public ProductionSuggestionResponse suggestProduction() {
        return service.suggestPlan();
    }
}
