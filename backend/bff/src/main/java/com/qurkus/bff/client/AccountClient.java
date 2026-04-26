package com.qurkus.bff.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@RegisterClientHeaders(AuthForwardingHeadersFactory.class)
@Path("/accounts")
public interface AccountClient {
    @GET
    @Path("/total-balance")
    @Produces(MediaType.APPLICATION_JSON)
    BalanceResponse totalBalance(@HeaderParam("Authorization") String authorization);

    record BalanceResponse(long totalBalanceCents) {}
}

