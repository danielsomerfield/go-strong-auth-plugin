package com.thoughtworks.go.strongauth.config;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.strongauth.ComponentFactory;
import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetailSource;
import com.thoughtworks.go.strongauth.authentication.principalDetailSources.ConfigurableUserPrincipalDetailSource;
import com.thoughtworks.go.strongauth.handlers.AuthenticationHandler;
import com.thoughtworks.go.strongauth.handlers.Handlers;
import com.thoughtworks.go.strongauth.handlers.PluginConfigurationHandler;
import com.thoughtworks.go.strongauth.handlers.PluginSettingsHandler;
import com.thoughtworks.go.strongauth.util.io.FileChangeMonitor;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.GoUserEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.File;

@Configuration
public class Config {

    @Bean
    public Handlers handlers() {
        return new Handlers(
                ImmutableMap.of(
                        ComponentFactory.CALL_FROM_SERVER_GET_CONFIGURATION, PluginSettingsHandler.getConfiguration(),
                        ComponentFactory.CALL_FROM_SERVER_GET_VIEW, PluginSettingsHandler.getView(),
                        ComponentFactory.CALL_FROM_SERVER_VALIDATE_CONFIGURATION, PluginSettingsHandler.validateConfiguration(),
                        ComponentFactory.CALL_FROM_SERVER_PLUGIN_CONFIGURATION, new PluginConfigurationHandler(),
                        ComponentFactory.CALL_FROM_SERVER_AUTHENTICATE_USER, new AuthenticationHandler(
                                requestDecoder(),
                                authenticator(),
                                goUserEncoder()
                        )
//            CALL_FROM_SERVER_SEARCH_USER, new SearchUserHandler(),
//            CALL_FROM_SERVER_INDEX, new PluginIndexRequestHandler(accessorWrapper, goPluginIdentifier)
                )
        );
    }

    @Bean
    @Autowired
    public ComponentFactory componentFactory(Handlers handlers) {
        return new ComponentFactory(handlers);
    }

    private GoAuthenticationRequestDecoder requestDecoder() {
        return new GoAuthenticationRequestDecoder();
    }

    private Authenticator authenticator() {
        return new Authenticator(principalSource());
    }

    private PrincipalDetailSource principalSource() {
        return new ConfigurableUserPrincipalDetailSource(new FileChangeMonitor(new File("/etc/go/passwd")));
    }

    private GoUserEncoder goUserEncoder() {
        return new GoUserEncoder();
    }

}
