package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetailSource;
import com.thoughtworks.go.strongauth.goAPI.GoUserAPI;
import com.thoughtworks.go.strongauth.handlers.AuthenticationHandler;
import com.thoughtworks.go.strongauth.handlers.Handler;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.RedirectResponseEncoder;

public class ComponentFactory {

    public static final String pluginId = Constants.PLUGIN_ID;

    public static Handler authenticationHandler() {
        return new AuthenticationHandler(
                requestDecoder(),
                authenticator(),
                goUserAPI(),
                redirectEncoder()
        );
    }

    private static Logger logger() {
        return Logger.getLoggerFor(ComponentFactory.class);
    }

    private static RedirectResponseEncoder redirectEncoder() {
        return new RedirectResponseEncoder();
    }

    private static GoUserAPI goUserAPI() {
        return new GoUserAPI();
    }

    private static Authenticator authenticator() {
        return new Authenticator(principalSource(), logger());
    }

    private static PrincipalDetailSource principalSource() {
        throw new UnsupportedOperationException("NYI");
    }

    private static GoAuthenticationRequestDecoder requestDecoder() {
        return new GoAuthenticationRequestDecoder();
    }
}
