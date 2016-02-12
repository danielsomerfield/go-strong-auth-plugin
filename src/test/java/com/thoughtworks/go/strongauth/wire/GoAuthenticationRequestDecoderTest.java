package com.thoughtworks.go.strongauth.wire;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GoAuthenticationRequestDecoderTest {

    @Test
    public void testDecodeAuthenticationValidRequest() {
        GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        when(request.requestBody()).thenReturn("{\"username\":\"uname\",\"password\":\"pword\"}");

        GoAuthenticationRequestDecoder decoder = new GoAuthenticationRequestDecoder();
        Optional<AuthenticationRequest> maybeRequest = decoder.decode(request);
        assertThat(maybeRequest, is(Optional.of(new AuthenticationRequest("uname", "pword"))));
    }

    @Test
    public void testDecodeToAbsentForInvalidRequest() {
        GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        when(request.requestBody()).thenReturn("{\"password\":\"pword\"}");

        GoAuthenticationRequestDecoder decoder = new GoAuthenticationRequestDecoder();
        Optional<AuthenticationRequest> maybeRequest = decoder.decode(request);
        assertThat(maybeRequest, is(Optional.<AuthenticationRequest>absent()));
    }

}