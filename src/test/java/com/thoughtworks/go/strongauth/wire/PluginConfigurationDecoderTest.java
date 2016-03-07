package com.thoughtworks.go.strongauth.wire;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.strongauth.config.PluginConfiguration;
import com.thoughtworks.go.strongauth.util.Json;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PluginConfigurationDecoderTest {

    @Test
    public void testValidConfiguration() {
        DefaultGoApiResponse response = new DefaultGoApiResponse(200);
        response.setResponseBody(Json.toJson(ImmutableMap.of("PASSWORD_FILE_PATH", "/foo/bar")));
        PluginConfiguration configuration = new PluginConfigurationDecoder().decode(response);
        assertThat(configuration, is(new PluginConfiguration("/foo/bar")));
    }

    @Test
    public void testHandleNon200() {
        DefaultGoApiResponse response = new DefaultGoApiResponse(500);
        PluginConfiguration configuration = new PluginConfigurationDecoder().decode(response);
        assertThat(configuration, is(new PluginConfiguration("/etc/go/passwd")));
    }

    @Test
    public void testNotConfigured() {
        DefaultGoApiResponse response = new DefaultGoApiResponse(200);
        PluginConfiguration configuration = new PluginConfigurationDecoder().decode(response);
        assertThat(configuration, is(new PluginConfiguration("/etc/go/passwd")));
    }

    @Test
    public void testNotConfiguredWithPath() {
        DefaultGoApiResponse response = new DefaultGoApiResponse(200);
        response.setResponseBody(Json.toJson(ImmutableMap.of()));
        PluginConfiguration configuration = new PluginConfigurationDecoder().decode(response);
        assertThat(configuration, is(new PluginConfiguration("/etc/go/passwd")));
    }
}