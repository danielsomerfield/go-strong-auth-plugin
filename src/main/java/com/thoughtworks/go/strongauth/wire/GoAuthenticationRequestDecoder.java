package com.thoughtworks.go.strongauth.wire;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;

import java.io.IOException;

public class GoAuthenticationRequestDecoder {

    private static Gson gson = new Gson();

    public Optional<AuthenticationRequest> decode(GoPluginApiRequest goRequest) {
        try {
            AuthenticationRequest request = gson.getAdapter(AuthenticationRequest.class).fromJson(goRequest.requestBody());
            return hasRequiredFields(request) ? Optional.of(request) : Optional.<AuthenticationRequest>absent();
        } catch (IOException e) {
            return Optional.absent();
        }
    }

    private boolean hasRequiredFields(AuthenticationRequest request) {
        return request.getPassword() != null && request.getUsername() != null;
    }

}
