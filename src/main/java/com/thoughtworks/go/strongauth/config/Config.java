package com.thoughtworks.go.strongauth.config;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.strongauth.ComponentFactory;
import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetailSource;
import com.thoughtworks.go.strongauth.authentication.principalDetailSources.ConfigurableUserPrincipalDetailSource;
import com.thoughtworks.go.strongauth.handlers.AuthenticationHandler;
import com.thoughtworks.go.strongauth.handlers.Handlers;
import com.thoughtworks.go.strongauth.handlers.PluginConfigurationHandler;
import com.thoughtworks.go.strongauth.handlers.PluginSettingsHandler;
import com.thoughtworks.go.strongauth.util.InputStreamSource;
import com.thoughtworks.go.strongauth.util.io.FileChangeMonitor;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.GoUserEncoder;
import com.thoughtworks.go.strongauth.wire.PluginConfigurationDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    private static final String CALL_FROM_SERVER_GET_CONFIGURATION = "go.plugin-settings.get-configuration";
    private static final String CALL_FROM_SERVER_AUTHENTICATE_USER = "go.authentication.authenticate-user";
    private static final String CALL_FROM_SERVER_PLUGIN_CONFIGURATION = "go.authentication.plugin-configuration";
    private static final String CALL_FROM_SERVER_GET_VIEW = "go.plugin-settings.get-view";
    private static final String CALL_FROM_SERVER_VALIDATE_CONFIGURATION = "go.plugin-settings.validate-configuration";

    @Bean
    Handlers handlers(final AuthenticationHandler authenticationHandler) {
        return new Handlers(
                ImmutableMap.of(
                        CALL_FROM_SERVER_GET_CONFIGURATION, PluginSettingsHandler.getConfiguration(),
                        CALL_FROM_SERVER_GET_VIEW, PluginSettingsHandler.getView(),
                        CALL_FROM_SERVER_VALIDATE_CONFIGURATION, PluginSettingsHandler.validateConfiguration(),
                        CALL_FROM_SERVER_PLUGIN_CONFIGURATION, new PluginConfigurationHandler(),
                        CALL_FROM_SERVER_AUTHENTICATE_USER, authenticationHandler
//            CALL_FROM_SERVER_SEARCH_USER, new SearchUserHandler(),
//            CALL_FROM_SERVER_INDEX, new PluginIndexRequestHandler(accessorWrapper, goPluginIdentifier)
                )
        );
    }

    @Bean
    AuthenticationHandler authenticationHandler(final Authenticator authenticator) {
        return new AuthenticationHandler(
                new GoAuthenticationRequestDecoder(),
                authenticator,
                new GoUserEncoder()
        );
    }

    @Bean
    Authenticator authenticator(final PrincipalDetailSource principalDetailSource) {
        return new Authenticator(principalDetailSource);
    }

    @Bean
    ComponentFactory componentFactory(final Handlers handlers) {
        return new ComponentFactory(handlers);
    }

    @Bean
    PrincipalDetailSource principalSource(final InputStreamSource inputStreamSource) {
        return new ConfigurableUserPrincipalDetailSource(inputStreamSource);
    }

    @Bean
    InputStreamSource inputStreamSource(final GoAPI goAPI) {
        return new FileChangeMonitor(goAPI.getPluginConfiguration().principalSourceFile());
    }


    @Bean
    GoAPI goAPI(@SuppressWarnings("SpringJavaAutowiringInspection") final GoPluginIdentifier goPluginIdentifier,
                @SuppressWarnings("SpringJavaAutowiringInspection") final GoApplicationAccessor goApplicationAccessor,
                final PluginConfigurationDecoder pluginConfigurationDecoder

    ) {
        return new GoAPI(goPluginIdentifier, goApplicationAccessor, pluginConfigurationDecoder);
    }

    @Bean
    PluginConfigurationDecoder pluginConfigurationDecoder() {
        return new PluginConfigurationDecoder();
    }

}
