package com.thoughtworks.go.strongauth.handlers;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static com.thoughtworks.go.strongauth.util.Json.toJson;
import static com.thoughtworks.go.strongauth.util.MapBuilder.create;

public class PluginConfigurationHandler implements Handler {
    private static final String CONFIG_WEB_AUTH = "supports-web-based-authentication";
    private static final String CONFIG_PASSWORD_AUTH = "supports-password-based-authentication";

    @Override
    public GoPluginApiResponse call(GoPluginApiRequest request) {
        String responseBody = toJson(create()
                .add(CONFIG_WEB_AUTH, false)
                .add(CONFIG_PASSWORD_AUTH, true)
                .build());

        return DefaultGoPluginApiResponse.success(responseBody);
    }
}
