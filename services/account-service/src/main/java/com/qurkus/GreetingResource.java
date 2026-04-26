package com.qurkus;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/accounts")
public class GreetingResource {

    @GET
    @Path("/total-balance")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("USER")
    public BalanceResponse totalBalance() {
        // TODO: Replace with JPA-backed sum(balance) for current user.
        return new BalanceResponse(500_000);
    }

    public record BalanceResponse(long totalBalanceCents) {}
}
