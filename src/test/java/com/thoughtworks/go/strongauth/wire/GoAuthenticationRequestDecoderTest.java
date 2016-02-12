package com.thoughtworks.go.strongauth.wire;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.request.DefaultGoPluginApiRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class GoAuthenticationRequestDecoderTest {

    @Test
    @Ignore
    public void testDecodeAuthenticationValidRequest() {
        GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        GoAuthenticationRequestDecoder decoder = new GoAuthenticationRequestDecoder();
        Optional<AuthenticationRequest> maybeRequest = decoder.decode(request);
    }

}