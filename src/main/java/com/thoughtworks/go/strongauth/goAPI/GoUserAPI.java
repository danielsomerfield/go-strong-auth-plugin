package com.thoughtworks.go.strongauth.goAPI;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;

import java.util.Map;

import static com.thoughtworks.go.strongauth.util.Json.toJson;

public class GoUserAPI {

    private final GoApplicationAccessor goApplicationAccessor;
    private final GoAPIMessageBuilder goAPIMessageBuilder;

    public GoUserAPI(
            GoApplicationAccessor goApplicationAccessor,
            GoAPIMessageBuilder goAPIMessageBuilder
    ) {
        this.goApplicationAccessor = goApplicationAccessor;
        this.goAPIMessageBuilder = goAPIMessageBuilder;
    }

    public void authenticateUser(GoUser user) {
        Map<String, ImmutableMap<String, String>> userMap = createUserMap(user);
        GoApiRequest request = goAPIMessageBuilder.createAPIRequest(
                "go.processor.authentication.authenticate-user",
                toJson(userMap)
        );
        goApplicationAccessor.submit(request);
    }

    private ImmutableMap<String, ImmutableMap<String, String>> createUserMap(GoUser user) {
        return ImmutableMap.of("user", ImmutableMap.of("username", user.getUsername()));
    }

}
