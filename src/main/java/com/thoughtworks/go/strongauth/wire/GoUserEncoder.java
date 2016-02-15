package com.thoughtworks.go.strongauth.wire;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.goAPI.GoUser;
import com.thoughtworks.go.strongauth.util.Json;

public class GoUserEncoder {

    public GoPluginApiResponse encode(GoUser user) {
        final DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(200);
        response.setResponseBody(Json.toJson(createUserMap(user)));
        return response;
    }

    public GoPluginApiResponse noUser() {
        return new DefaultGoPluginApiResponse(200);
    }

    private ImmutableMap<String, ImmutableMap<String, String>> createUserMap(GoUser user) {
        return ImmutableMap.of("user", ImmutableMap.of("username", user.getUsername()));
    }
}
