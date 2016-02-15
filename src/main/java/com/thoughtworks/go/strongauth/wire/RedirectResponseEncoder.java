package com.thoughtworks.go.strongauth.wire;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.net.URI;
import java.util.Map;

import static java.lang.String.format;

public class RedirectResponseEncoder {
    private final URI serverBase;

    public RedirectResponseEncoder(URI serverBase) {
        this.serverBase = serverBase;
    }

    public GoPluginApiResponse redirectToServerBase() {
        return createRedirectMessage("/");
    }

    private GoPluginApiResponse createRedirectMessage(final String path) {
        return new GoPluginApiResponse() {
            @Override
            public int responseCode() {
                return 302;
            }

            @Override
            public Map<String, String> responseHeaders() {
                return ImmutableMap.of("Location", format("%s%s", serverBase, path));
            }

            @Override
            public String responseBody() {
                return null;
            }
        };
    }

    public GoPluginApiResponse redirectToLoginPage() {
        return createRedirectMessage("/go/auth/login");
    }
}
