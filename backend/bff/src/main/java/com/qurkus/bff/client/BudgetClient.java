package com.qurkus.bff.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@RegisterClientHeaders(AuthForwardingHeadersFactory.class)
@Path("/budgets")
public interface BudgetClient {
    @GET
    @Path("/monthly")
    @Produces(MediaType.APPLICATION_JSON)
    MonthlyBudgetResponse monthlyBudget(
            @QueryParam("month") String month,
            @HeaderParam("Authorization") String authorization
    );

    record MonthlyBudgetResponse(long budgetCents) {}
}

