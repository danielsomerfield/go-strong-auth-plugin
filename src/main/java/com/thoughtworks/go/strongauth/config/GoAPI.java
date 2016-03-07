package com.thoughtworks.go.strongauth.config;

import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.response.GoApiResponse;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.go.strongauth.util.Json;
import com.thoughtworks.go.strongauth.wire.PluginConfigurationDecoder;


public class GoAPI {
    public final String pluginId = Constants.PLUGIN_ID;
    private static final Logger LOGGER = Logger.getLoggerFor(GoAPI.class);
    private final GoPluginIdentifier goPluginIdentifier;
    private final GoApplicationAccessor goApplicationAccessor;
    private final PluginConfigurationDecoder pluginConfigurationDecoder;

    public GoAPI(
            GoPluginIdentifier goPluginIdentifier,
            GoApplicationAccessor goApplicationAccessor,
            PluginConfigurationDecoder pluginConfigurationDecoder
    ) {
        this.goPluginIdentifier = goPluginIdentifier;
        this.goApplicationAccessor = goApplicationAccessor;
        this.pluginConfigurationDecoder = pluginConfigurationDecoder;
    }

    public PluginConfiguration getPluginConfiguration() {
        DefaultGoApiRequest request = new DefaultGoApiRequest("go.processor.plugin-settings.get", "1.0", goPluginIdentifier);
        request.setRequestBody(Json.toJson(ImmutableMap.of("plugin-id", Constants.PLUGIN_ID)));
        GoApiResponse response = goApplicationAccessor.submit(request);
        return pluginConfigurationDecoder.decode(response);
    }

}
