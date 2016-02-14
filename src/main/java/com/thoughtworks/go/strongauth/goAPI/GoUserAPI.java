package com.thoughtworks.go.strongauth.goAPI;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;
import com.thoughtworks.go.strongauth.util.Json;

import java.util.Map;

public class GoUserAPI {

    private final GoPluginIdentifier goPluginIdentifier;
    private final GoApplicationAccessor goApplicationAccessor;

    public GoUserAPI(
            GoPluginIdentifier goPluginIdentifier,
            GoApplicationAccessor goApplicationAccessor

    ) {
        this.goPluginIdentifier = goPluginIdentifier;
        this.goApplicationAccessor = goApplicationAccessor;
    }

    public void authenticateUser(GoUser user) {
        Map<String, String> userMap = ImmutableMap.of("username", user.getUsername());
        GoApiRequest request = createAuthenticatUserRequest(userMap);
        goApplicationAccessor.submit(request);
    }

    private GoApiRequest createAuthenticatUserRequest(Map<String, String> userMap) {
        Map<String, ?> messageMap = ImmutableMap.of("user", userMap);
        String messageString = Json.toJson(messageMap);
        DefaultGoApiRequest authRequest = new DefaultGoApiRequest("go.processor.authentication.authenticate-user",
                "1.0",
                goPluginIdentifier
        );
        authRequest.setRequestBody(messageString);
        return authRequest;
    }

}
