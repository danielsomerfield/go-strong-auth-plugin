package com.thoughtworks.go.strongauth.wire;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.util.Json;

public class GoUserEncoder {

    public GoPluginApiResponse encode(final String username) {
        final DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(200);
        final ImmutableMap<String, ImmutableMap<String, String>> map =
                ImmutableMap.of("user", ImmutableMap.of("username", username));
        response.setResponseBody(Json.toJson(map));
        return response;
    }

    public GoPluginApiResponse noUser() {
        return new DefaultGoPluginApiResponse(200);
    }

}
