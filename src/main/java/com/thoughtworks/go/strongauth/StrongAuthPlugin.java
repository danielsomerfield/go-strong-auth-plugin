package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.AbstractGoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

@Extension
public class StrongAuthPlugin extends AbstractGoPlugin {

    private static final List<String> SUPPORTED_VERSIONS = singletonList("1.0");

    public static final String EXTENSION_NAME = "strongauth";
    public static final String PLUGIN_SETTINGS_GET_CONFIGURATION = "go.plugin-settings.get-configuration";

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest requestMessage) throws UnhandledRequestTypeException {
        Logger.getLoggerFor(StrongAuthPlugin.class).warn(String.format("Received message: %s -- %s -- %s", requestMessage.requestParameters(), requestMessage.requestName(), requestMessage.requestBody()));

        if (requestMessage.requestName().equals(PLUGIN_SETTINGS_GET_CONFIGURATION)) {
            return handleGetPluginSettingsConfiguration();
        }
        return new DefaultGoPluginApiResponse(404);
    }

    private GoPluginApiResponse handleGetPluginSettingsConfiguration() {
        return new GoPluginApiResponse() {
            @Override
            public int responseCode() {
                return 200;
            }

            @Override
            public Map<String, String> responseHeaders() {
                return new HashMap<>();
            }

            @Override
            public String responseBody() {
                Map<String, Object> configuration = new HashMap<>();
                configuration.put("display-name", "Strong Auth Plugin");
                configuration.put("display-image-url", "http://icons.iconarchive.com/icons/saki/snowish/32/Authentication-Lock-icon.png");
                configuration.put("supports-web-based-authentication", true);
                configuration.put("supports-password-based-authentication", true);
                return JSONUtils.toJSON(configuration);
            }
        };
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier(EXTENSION_NAME, SUPPORTED_VERSIONS);
    }
}