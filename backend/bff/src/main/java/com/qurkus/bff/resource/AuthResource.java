package com.qurkus.bff.resource;

import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Duration;
import java.util.Set;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    public record LoginResponse(String token) {}

    @POST
    @Path("/login")
    @PermitAll
    public Response login(LoginRequest req) {
        // Demo-only. Replace with real identity provider / user store.
        if (!"user".equals(req.username()) || !"password".equals(req.password())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = Jwt.issuer("qurkus-local")
                .upn(req.username())
                .subject(req.username())
                .groups(Set.of("USER"))
                .expiresIn(Duration.ofHours(1))
                .sign();

        return Response.ok(new LoginResponse(token)).build();
    }
}

