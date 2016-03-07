package com.thoughtworks.go.strongauth.config;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse;
import com.thoughtworks.go.strongauth.wire.PluginConfigurationDecoder;
import org.hamcrest.CustomMatcher;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONCompare.compareJSON;

public class GoAPITest {
    final GoPluginIdentifier goPluginIdentifier = new GoPluginIdentifier("authentication", singletonList("1.0"));
    final GoApplicationAccessor goApplicationAccessor = mock(GoApplicationAccessor.class);
    final PluginConfigurationDecoder pluginConfigurationDecoder = new PluginConfigurationDecoder();

    @Test
    public void testRequestPluginSettingsForStrongAuth() {
        DefaultGoApiResponse response = new DefaultGoApiResponse(200);
        response.setResponseBody(new JSONObject().put("PASSWORD_FILE_PATH", "/path/to/thing").toString());

        when(goApplicationAccessor.submit(requestWithJSON(new JSONObject().put("plugin-id", "strongauth"))))
                .thenReturn(response);

        GoAPI goAPI = new GoAPI(goPluginIdentifier, goApplicationAccessor, pluginConfigurationDecoder);

        PluginConfiguration maybeConfiguration = goAPI.getPluginConfiguration();
        assertThat(maybeConfiguration.getPrincipalSourceFile(), is(new File("/path/to/thing")));
    }

    @Test
    public void testPluginSettingsWhenRequestToServerFails() {
        DefaultGoApiResponse badResponse = new DefaultGoApiResponse(404);
        when(goApplicationAccessor.submit(requestWithJSON(new JSONObject().put("plugin-id", "strongauth"))))
                .thenReturn(badResponse);

        GoAPI goAPI = new GoAPI(goPluginIdentifier, goApplicationAccessor, pluginConfigurationDecoder);

        PluginConfiguration maybeConfiguration = goAPI.getPluginConfiguration();
        assertThat(maybeConfiguration.getPrincipalSourceFile(), is(new File("/etc/go/passwd")));
    }


    private static GoApiRequest requestWithJSON(final JSONObject json) {
        return argThat(new CustomMatcher<GoApiRequest>(json.toString()) {
            @Override
            public boolean matches(Object item) {
                final String expectedJSONString = ((GoApiRequest) item).requestBody();
                return compareJSON(new JSONObject(expectedJSONString), json, JSONCompareMode.STRICT).passed();
            }
        });
    }
}