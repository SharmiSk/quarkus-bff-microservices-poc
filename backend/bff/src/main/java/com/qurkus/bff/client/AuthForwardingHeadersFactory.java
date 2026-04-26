package com.qurkus.bff.client;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

public class AuthForwardingHeadersFactory implements ClientHeadersFactory {

    @Override
    public MultivaluedMap<String, String> update(
            MultivaluedMap<String, String> incomingHeaders,
            MultivaluedMap<String, String> clientOutgoingHeaders
    ) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>(clientOutgoingHeaders);
        if (incomingHeaders != null) {
            var auth = incomingHeaders.getFirst("Authorization");
            if (auth == null) {
                auth = incomingHeaders.getFirst("authorization");
            }
            if (auth != null && !auth.isBlank()) {
                result.putSingle("Authorization", auth);
            }
        }
        return result;
    }
}

