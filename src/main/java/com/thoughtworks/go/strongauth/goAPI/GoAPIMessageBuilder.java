package com.thoughtworks.go.strongauth.goAPI;

import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;

public class GoAPIMessageBuilder {
    private final GoPluginIdentifier goPluginIdentifier;

    public GoAPIMessageBuilder(GoPluginIdentifier goPluginIdentifier) {
        this.goPluginIdentifier = goPluginIdentifier;
    }

    public DefaultGoApiRequest createAPIRequest(String messageName, String messageString) {
        DefaultGoApiRequest request = new DefaultGoApiRequest(messageName,
                "1.0",
                goPluginIdentifier
        );
        request.setRequestBody(messageString);
        return request;
    }
}
