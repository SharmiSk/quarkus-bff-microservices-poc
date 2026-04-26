package com.qurkus;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/budgets")
public class GreetingResource {

    @GET
    @Path("/monthly")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER")
    public MonthlyBudgetResponse monthlyBudget(@QueryParam("month") String month) {
        // TODO: Replace with JPA-backed budget config for current user/month.
        return new MonthlyBudgetResponse(200_00);
    }

    public record MonthlyBudgetResponse(long budgetCents) {}
}
