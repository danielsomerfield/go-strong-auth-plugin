package com.thoughtworks.go.strongauth;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetail;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetailSource;
import com.thoughtworks.go.strongauth.goAPI.GoApplicationAccessorSource;
import com.thoughtworks.go.strongauth.goAPI.GoUserAPI;
import com.thoughtworks.go.strongauth.handlers.*;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.RedirectResponseEncoder;

import static java.util.Arrays.asList;

public class ComponentFactory {

    private static final String CALL_FROM_SERVER_GET_CONFIGURATION = "go.plugin-settings.get-configuration";
    private static final String CALL_FROM_SERVER_AUTHENTICATE_USER = "go.authentication.authenticate-user";
    private static final String CALL_FROM_SERVER_PLUGIN_CONFIGURATION = "go.authentication.plugin-configuration";
    private static final String CALL_FROM_SERVER_GET_VIEW = "go.plugin-settings.get-view";
    private static final String CALL_FROM_SERVER_VALIDATE_CONFIGURATION = "go.plugin-settings.validate-configuration";

    public final String pluginId = Constants.PLUGIN_ID;
    private final GoApplicationAccessorSource goApplicationAccessor;

    private GoPluginIdentifier goPluginIdentifier = new GoPluginIdentifier("authentication", asList("1.0"));

    public ComponentFactory(GoApplicationAccessorSource goApplicationAccessorSource) {
        this.goApplicationAccessor = goApplicationAccessorSource;
    }

    public Handler authenticationHandler() {
        return new AuthenticationHandler(
                requestDecoder(),
                authenticator(),
                goUserAPI(),
                redirectEncoder()
        );
    }

    private Logger logger() {
        return Logger.getLoggerFor(ComponentFactory.class);
    }

    private RedirectResponseEncoder redirectEncoder() {
        return new RedirectResponseEncoder();
    }

    private GoUserAPI goUserAPI() {
        return new GoUserAPI(goPluginIdentifier(), goApplicationAccessor());
    }

    private GoApplicationAccessor goApplicationAccessor() {
        return this.goApplicationAccessor.getGoApplicationAccessor();
    }

    private Authenticator authenticator() {
        return new Authenticator(principalSource(), logger());
    }

    private PrincipalDetailSource principalSource() {
        return new PrincipalDetailSource() {
            @Override
            public Optional<PrincipalDetail> byUsername(String username) {
                throw new UnsupportedOperationException("NYI");
            }
        };
    }

    private GoAuthenticationRequestDecoder requestDecoder() {
        return new GoAuthenticationRequestDecoder();
    }

    public GoPluginIdentifier goPluginIdentifier() {
        return goPluginIdentifier;
    }

    public Handlers handlers() {
        return new Handlers(
                ImmutableMap.of(
                        CALL_FROM_SERVER_GET_CONFIGURATION, PluginSettingsHandler.getConfiguration(),
                        CALL_FROM_SERVER_GET_VIEW, PluginSettingsHandler.getView(),
                        CALL_FROM_SERVER_VALIDATE_CONFIGURATION, PluginSettingsHandler.validateConfiguration(),
                        CALL_FROM_SERVER_PLUGIN_CONFIGURATION, new PluginConfigurationHandler(),
                        CALL_FROM_SERVER_AUTHENTICATE_USER, authenticationHandler()
//            CALL_FROM_SERVER_SEARCH_USER, new SearchUserHandler(),
//            CALL_FROM_SERVER_INDEX, new PluginIndexRequestHandler(accessorWrapper, goPluginIdentifier)
                )
        );
    }
}
