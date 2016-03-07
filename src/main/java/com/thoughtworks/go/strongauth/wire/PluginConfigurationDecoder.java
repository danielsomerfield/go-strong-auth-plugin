package com.thoughtworks.go.strongauth.wire;

import com.thoughtworks.go.plugin.api.response.GoApiResponse;
import com.thoughtworks.go.strongauth.config.PluginConfiguration;
import com.thoughtworks.go.strongauth.util.Json;

import java.util.Map;

public class PluginConfigurationDecoder {
    public PluginConfiguration decode(GoApiResponse response) {
        if (response.responseCode() == 200) {
            return parseConfiguration(response.responseBody());
        } else {
            return defaultConfiguration();
        }
    }

    private PluginConfiguration parseConfiguration(String body) {
        if (body == null) {
            return defaultConfiguration();
        } else {
            return fromMap(Json.toMapOfStrings(body));
        }
    }

    private PluginConfiguration fromMap(Map<String, String> configMap) {
        return new PluginConfiguration(configMap.get("PASSWORD_FILE_PATH"));
    }

    public PluginConfiguration defaultConfiguration() {
        return new PluginConfiguration();
    }
}
