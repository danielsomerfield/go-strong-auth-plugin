package com.thoughtworks.go.strongauth.goAPI;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GoUserAPITest {

    @Test
    public void testAuthenticateUser() throws Exception {
        final GoApplicationAccessor goApplicationAccessor = mock(GoApplicationAccessor.class);
        final GoPluginIdentifier goPluginIdentifier = mock(GoPluginIdentifier.class);
        when(goPluginIdentifier.getExtension()).thenReturn("authentication");

        GoUser goUser = new GoUser("go-uzer");
        GoUserAPI goUserAPI = new GoUserAPI(goPluginIdentifier, goApplicationAccessor);
        goUserAPI.authenticateUser(goUser);

        final ArgumentCaptor<GoApiRequest> requestCaptor = ArgumentCaptor.forClass(GoApiRequest.class);
        final JSONObject expected = new JSONObject().put("user", new JSONObject().put("username", "go-uzer"));
        verify(goApplicationAccessor).submit(requestCaptor.capture());
        JSONAssert.assertEquals(expected.toString(), requestCaptor.getValue().requestBody(), true);
        assertThat(requestCaptor.getValue().api(), is("go.processor.authentication.authenticate-user"));
        assertThat(requestCaptor.getValue().pluginIdentifier(), is(goPluginIdentifier));
        assertThat(requestCaptor.getValue().apiVersion(), is("1.0"));
    }

}