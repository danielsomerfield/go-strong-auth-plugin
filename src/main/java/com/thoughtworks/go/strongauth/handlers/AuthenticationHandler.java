package com.thoughtworks.go.strongauth.handlers;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.config.Option;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;
import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.Principal;
import com.thoughtworks.go.strongauth.goAPI.GoUser;
import com.thoughtworks.go.strongauth.goAPI.GoUserAPI;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.RedirectResponseEncoder;

public class AuthenticationHandler implements Handler {
    private final GoAuthenticationRequestDecoder requestDecoder;
    private final Authenticator authenticator;
    private final GoUserAPI goUserAPI;
    private final RedirectResponseEncoder redirectResponseEncoder;

    public AuthenticationHandler(
            GoAuthenticationRequestDecoder requestDecoder,
            Authenticator authenticator,
            GoUserAPI goUserAPI,
            RedirectResponseEncoder redirectResponseEncoder) {

        this.requestDecoder = requestDecoder;
        this.authenticator = authenticator;
        this.goUserAPI = goUserAPI;
        this.redirectResponseEncoder = redirectResponseEncoder;
    }

    public static <T, U> Optional<U> flatMap(Optional<T> maybeValue, final Function<T, Optional<U>> transformation) {
        return maybeValue.isPresent() ? transformation.apply(maybeValue.get()) : Optional.<U>absent();
    }

    @Override
    public GoPluginApiResponse call(GoPluginApiRequest request) {
        final Optional<AuthenticationRequest> maybeAuthRequest = requestDecoder.decode(request);

        Optional<Principal> maybePrincipal = flatMap(maybeAuthRequest, new Function<AuthenticationRequest, Optional<Principal>>() {
            @Override
            public Optional<Principal> apply(AuthenticationRequest authRequest) {
                return authenticator.authenticate(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                );
            }
        });

        if (maybePrincipal.isPresent()) {
            goUserAPI.authenticateUser(new GoUser(maybePrincipal.get().getId()));
            return redirectResponseEncoder.redirectToServerBase();
        } else {
            return redirectResponseEncoder.redirectToLoginPage();
        }
    }
}
