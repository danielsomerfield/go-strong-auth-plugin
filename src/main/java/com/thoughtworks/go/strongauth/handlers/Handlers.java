package com.thoughtworks.go.strongauth.handlers;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.strongauth.ComponentFactory;

import java.util.Map;

import static java.lang.String.format;

public class Handlers {
    private static final String CALL_FROM_SERVER_GET_CONFIGURATION = "go.plugin-settings.get-configuration";
    private static final String CALL_FROM_SERVER_AUTHENTICATE_USER = "go.authentication.authenticate-user";
    private static final String CALL_FROM_SERVER_PLUGIN_CONFIGURATION = "go.authentication.plugin-configuration";
    private static final String CALL_FROM_SERVER_GET_VIEW = "go.plugin-settings.get-view";
    private static final String CALL_FROM_SERVER_VALIDATE_CONFIGURATION = "go.plugin-settings.validate-configuration";

    private Map<String, Handler> handlers = ImmutableMap.of(
            CALL_FROM_SERVER_GET_CONFIGURATION, PluginSettingsHandler.getConfiguration(),
            CALL_FROM_SERVER_GET_VIEW, PluginSettingsHandler.getView(),
            CALL_FROM_SERVER_VALIDATE_CONFIGURATION, PluginSettingsHandler.validateConfiguration(),
            CALL_FROM_SERVER_PLUGIN_CONFIGURATION, new PluginConfigurationHandler(),
            CALL_FROM_SERVER_AUTHENTICATE_USER, ComponentFactory.authenticationHandler()
//            CALL_FROM_SERVER_SEARCH_USER, new SearchUserHandler(),
//            CALL_FROM_SERVER_INDEX, new PluginIndexRequestHandler(accessorWrapper, goPluginIdentifier)
    );

    public Handler get(String requestName) {
        if (handlers.containsKey(requestName)) {
            return handlers.get(requestName);
        }
        throw new RuntimeException(format("Handler for request of type: %s is not implemented", requestName));
    }
}
