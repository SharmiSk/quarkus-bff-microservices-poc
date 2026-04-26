package com.qurkus.bff.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient
@RegisterClientHeaders(AuthForwardingHeadersFactory.class)
@Path("/transactions")
public interface TransactionClient {
    @GET
    @Path("/monthly-spend")
    @Produces(MediaType.APPLICATION_JSON)
    MonthlySpendResponse monthlySpend(
            @QueryParam("month") String month,
            @HeaderParam("Authorization") String authorization
    );

    @GET
    @Path("/category-breakdown")
    @Produces(MediaType.APPLICATION_JSON)
    CategoryBreakdownResponse categoryBreakdown(
            @QueryParam("month") String month,
            @HeaderParam("Authorization") String authorization
    );

    record MonthlySpendResponse(long monthlySpendCents) {}
    record CategoryBreakdownResponse(List<CategorySpend> categories) {}
    record CategorySpend(String category, long spendCents) {}
}

