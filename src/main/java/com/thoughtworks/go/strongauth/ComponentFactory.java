package com.thoughtworks.go.strongauth;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetailSource;
import com.thoughtworks.go.strongauth.authentication.principalDetailSources.ConfigurableUserPrincipalDetailSource;
import com.thoughtworks.go.strongauth.goAPI.GoAPIMessageBuilder;
import com.thoughtworks.go.strongauth.goAPI.GoApplicationAccessorSource;
import com.thoughtworks.go.strongauth.goAPI.GoUserAPI;
import com.thoughtworks.go.strongauth.handlers.*;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.go.strongauth.util.io.FileChangeMonitor;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.GoUserEncoder;
import com.thoughtworks.go.strongauth.wire.RedirectResponseEncoder;

import java.io.File;
import java.net.URI;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class ComponentFactory {

    private static final String CALL_FROM_SERVER_GET_CONFIGURATION = "go.plugin-settings.get-configuration";
    private static final String CALL_FROM_SERVER_AUTHENTICATE_USER = "go.authentication.authenticate-user";
    private static final String CALL_FROM_SERVER_PLUGIN_CONFIGURATION = "go.authentication.plugin-configuration";
    private static final String CALL_FROM_SERVER_GET_VIEW = "go.plugin-settings.get-view";
    private static final String CALL_FROM_SERVER_VALIDATE_CONFIGURATION = "go.plugin-settings.validate-configuration";

    private static final Logger LOGGER = Logger.getLoggerFor(ComponentFactory.class);
    public final String pluginId = Constants.PLUGIN_ID;

    private final GoApplicationAccessorSource goApplicationAccessor;
    private final Handler CONFIGURATION_HANDLER = PluginSettingsHandler.getConfiguration();
    private final Handler PLUGIN_SETTINGS_IEW_HANDLER = PluginSettingsHandler.getView();
    private final Handler VALIDATE_CONFIGURATION_HANDLER = PluginSettingsHandler.validateConfiguration();
    private final PluginConfigurationHandler PLUGIN_CONFIGURATION_HANDLER = new PluginConfigurationHandler();
    private final AuthenticationHandler AUTHENTICATION_HANDLER = new AuthenticationHandler(
            requestDecoder(),
            authenticator(),
            goUserEncoder()
    );
    private final Handlers HANDLERS = new Handlers(
            ImmutableMap.of(
                    CALL_FROM_SERVER_GET_CONFIGURATION, CONFIGURATION_HANDLER,
                    CALL_FROM_SERVER_GET_VIEW, PLUGIN_SETTINGS_IEW_HANDLER,
                    CALL_FROM_SERVER_VALIDATE_CONFIGURATION, VALIDATE_CONFIGURATION_HANDLER,
                    CALL_FROM_SERVER_PLUGIN_CONFIGURATION, PLUGIN_CONFIGURATION_HANDLER,
                    CALL_FROM_SERVER_AUTHENTICATE_USER, AUTHENTICATION_HANDLER
//            CALL_FROM_SERVER_SEARCH_USER, new SearchUserHandler(),
//            CALL_FROM_SERVER_INDEX, new PluginIndexRequestHandler(accessorWrapper, goPluginIdentifier)
            )
    );

    private GoPluginIdentifier goPluginIdentifier = new GoPluginIdentifier("authentication", asList("1.0"));

    public ComponentFactory(GoApplicationAccessorSource goApplicationAccessorSource) {
        this.goApplicationAccessor = goApplicationAccessorSource;
    }

    private GoUserEncoder goUserEncoder() {
        return new GoUserEncoder();
    }

    private RedirectResponseEncoder redirectEncoder() {
        return new RedirectResponseEncoder(getServerRoot());
    }

    private URI getServerRoot() {
        return URI.create("http://192.168.99.100:8153");
    }

    private GoUserAPI goUserAPI() {
        return new GoUserAPI(goApplicationAccessor(), goAPIMessageBuilder());
    }

    private GoAPIMessageBuilder goAPIMessageBuilder() {
        return new GoAPIMessageBuilder(goPluginIdentifier());
    }

    private GoApplicationAccessor goApplicationAccessor() {
        return this.goApplicationAccessor.getGoApplicationAccessor();
    }

    private Authenticator authenticator() {
        return new Authenticator(principalSource());
    }

    private PrincipalDetailSource principalSource() {
        return new ConfigurableUserPrincipalDetailSource(new FileChangeMonitor(principalSourceFile()));
    }

    private File principalSourceFile() {
        return new File("/etc/go/passwd");
    }

    private GoAuthenticationRequestDecoder requestDecoder() {
        return new GoAuthenticationRequestDecoder();
    }

    public GoPluginIdentifier goPluginIdentifier() {
        return goPluginIdentifier;
    }

    public Handlers handlers() {
        return HANDLERS;
    }
}
