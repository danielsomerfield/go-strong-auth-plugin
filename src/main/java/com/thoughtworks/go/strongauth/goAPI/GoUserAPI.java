package com.thoughtworks.go.strongauth.goAPI;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;
import com.thoughtworks.go.strongauth.util.Json;

import java.util.Map;

public class GoUserAPI {
    public void authenticateUser(GoUser user) {
        Map<String, String> userMap = ImmutableMap.of("username", user.getUsername());
        GoApiRequest request = createAuthenticatUserRequest(userMap);
    }

    private GoApiRequest createAuthenticatUserRequest(Map<String, String> userMap) {
        Map<String, ?> messageMap = ImmutableMap.of("user", userMap);
        String messageString = Json.toJson(messageMap);
//        return new DefaultGoApiRequest("go.processor.authentication.authenticate-user", "1.0", )
        throw new UnsupportedOperationException("NYI");
    }

}
