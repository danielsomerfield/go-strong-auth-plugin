package com.thoughtworks.go.strongauth.handlers;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class AuthenticationHandler implements Handler {
    private static Logger LOGGER = Logger.getLoggerFor(AuthenticationHandler.class);

    @Override
    public GoPluginApiResponse call(GoPluginApiRequest request) {
        LOGGER.info("AuthenticationHandler.call");
        return DefaultGoPluginApiResponse.success("");
    }
}
