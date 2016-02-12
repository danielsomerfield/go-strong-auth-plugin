package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetailSource;
import com.thoughtworks.go.strongauth.goAPI.GoUserAPI;
import com.thoughtworks.go.strongauth.handlers.AuthenticationHandler;
import com.thoughtworks.go.strongauth.handlers.Handler;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.RedirectResponseEncoder;

public class ComponentFactory {
    public static Handler authenticationHandler() {
        return new AuthenticationHandler(
                requestDecoder(),
                authenticator(),
                goUserAPI(),
                redirectEncoder()
        );
    }

    private static RedirectResponseEncoder redirectEncoder() {
        return new RedirectResponseEncoder();
    }

    private static GoUserAPI goUserAPI() {
        return new GoUserAPI();
    }

    private static Authenticator authenticator() {
        return new Authenticator(principalSource());
    }

    private static PrincipalDetailSource principalSource() {
        throw new UnsupportedOperationException("NYI");
    }

    private static GoAuthenticationRequestDecoder requestDecoder() {
        return new GoAuthenticationRequestDecoder();
    }
}
