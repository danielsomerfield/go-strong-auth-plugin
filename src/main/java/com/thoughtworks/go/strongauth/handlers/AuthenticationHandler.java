package com.thoughtworks.go.strongauth.handlers;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;
import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.Principal;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.GoUserEncoder;
import com.thoughtworks.util.Functional;

public class AuthenticationHandler implements Handler {
    private final GoAuthenticationRequestDecoder requestDecoder;
    private final Authenticator authenticator;
    private final GoUserEncoder userEncoder;

    public AuthenticationHandler(
            GoAuthenticationRequestDecoder requestDecoder,
            Authenticator authenticator, GoUserEncoder userEncoder) {

        this.requestDecoder = requestDecoder;
        this.authenticator = authenticator;
        this.userEncoder = userEncoder;
    }

    @Override
    public GoPluginApiResponse call(GoPluginApiRequest request) {
        final Optional<AuthenticationRequest> maybeAuthRequest = requestDecoder.decode(request);

        Optional<Principal> maybePrincipal = Functional.flatMap(maybeAuthRequest, new Function<AuthenticationRequest, Optional<Principal>>() {
            @Override
            public Optional<Principal> apply(AuthenticationRequest authRequest) {
                return authenticator.authenticate(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                );
            }
        });

        if (maybePrincipal.isPresent()) {
            return userEncoder.encode(maybePrincipal.get().getId());
        } else {
            return userEncoder.noUser();
        }
    }
}
