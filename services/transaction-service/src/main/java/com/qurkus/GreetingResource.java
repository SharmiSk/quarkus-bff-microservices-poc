package com.qurkus;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/transactions")
public class GreetingResource {

    @GET
    @Path("/monthly-spend")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER")
    public MonthlySpendResponse monthlySpend(@QueryParam("month") String month) {
        // TODO: Replace with JPA-backed SUM(amount) filtered by user + month.
        return new MonthlySpendResponse(123_45);
    }

    @GET
    @Path("/category-breakdown")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER")
    public CategoryBreakdownResponse categoryBreakdown(@QueryParam("month") String month) {
        // TODO: Replace with JPA-backed GROUP BY category filtered by user + month.
        return new CategoryBreakdownResponse(java.util.List.of(
                new CategorySpend("Food", 50_00),
                new CategorySpend("Transport", 25_00),
                new CategorySpend("Bills", 48_45)
        ));
    }

    public record MonthlySpendResponse(long monthlySpendCents) {}
    public record CategoryBreakdownResponse(java.util.List<CategorySpend> categories) {}
    public record CategorySpend(String category, long spendCents) {}
}
