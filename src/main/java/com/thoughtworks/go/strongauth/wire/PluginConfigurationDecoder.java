package com.thoughtworks.go.strongauth.wire;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.GoApiResponse;
import com.thoughtworks.go.strongauth.config.PluginConfiguration;
import com.thoughtworks.go.strongauth.util.Json;

import java.util.Map;

public class PluginConfigurationDecoder {
    public Optional<PluginConfiguration> decode(GoApiResponse response) {
        Optional<PluginConfiguration> maybeConfiguration;
        if (response.responseCode() == 200) {
            maybeConfiguration = parseConfiguration(response.responseBody());
        } else {
            maybeConfiguration = Optional.absent();
        }
        return maybeConfiguration;
    }

    private Optional<PluginConfiguration> parseConfiguration(String body) {
        if (body == null) {
            return Optional.of(new PluginConfiguration());
        } else {
            return Optional.of(fromMap(Json.toMapOfStrings(body)));
        }
    }

    private PluginConfiguration fromMap(Map<String, String> configMap) {
        return new PluginConfiguration(configMap.get("PASSWORD_FILE_PATH"));
    }
}
