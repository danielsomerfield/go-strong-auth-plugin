package com.thoughtworks.go.strongauth.handlers;

import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public class SearchUserHandler implements Handler {
    private static Logger LOGGER = Logger.getLoggerFor(SearchUserHandler.class);

    @Override
    public GoPluginApiResponse call(GoPluginApiRequest request) {
        LOGGER.info("SearchUserHandler.call");
        return DefaultGoPluginApiResponse.success("");
    }
}
