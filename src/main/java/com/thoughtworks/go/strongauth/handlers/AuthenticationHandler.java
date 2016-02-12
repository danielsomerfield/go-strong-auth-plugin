package com.thoughtworks.go.strongauth.handlers;

import com.google.common.base.Optional;
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
    //    private static Logger LOGGER = Logger.getLoggerFor(AuthenticationHandler.class);
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

    @Override
    public GoPluginApiResponse call(GoPluginApiRequest request) {
        final AuthenticationRequest authenticationRequest = requestDecoder.decode(request);
        Optional<Principal> maybePrincipal = authenticator.authenticate(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        if (maybePrincipal.isPresent()) {
            goUserAPI.authenticateUser(new GoUser(maybePrincipal.get().getId()));
            return redirectResponseEncoder.redirectToServerBase();
        } else {
            return redirectResponseEncoder.redirectToLoginPage();
        }
    }
}
