package com.thoughtworks.go.strongauth.config;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;

public class GoAPITest {

    @Test
    public void testRequestPluginSettingsForStrongAuth() {

        GoPluginIdentifier goPluginIdentifier = new GoPluginIdentifier("authentication", singletonList("1.0"));
        GoApplicationAccessor goApplicationAccessor = mock(GoApplicationAccessor.class);

        GoAPI goAPI = new GoAPI();

        PluginConfiguration configuration = goAPI.getPluginConfiguration();
//        assertThat(configuration, is(new PluginConfiguration()));
    }

}