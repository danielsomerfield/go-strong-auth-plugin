package com.thoughtworks.go.strongauth.handlers;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;
import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.Principal;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.GoUserEncoder;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationHandlerTest {

    private GoAuthenticationRequestDecoder requestDecoder = mock(GoAuthenticationRequestDecoder.class);
    private Authenticator authenticator = mock(Authenticator.class);
    private GoUserEncoder userEncoder = mock(GoUserEncoder.class);

    private AuthenticationHandler authenticationHandler = new AuthenticationHandler(
            requestDecoder,
            authenticator,
            userEncoder
    );

    GoPluginApiResponse loginSuccessResponse = mock(GoPluginApiResponse.class);
    GoPluginApiResponse loginFailedResponse = mock(GoPluginApiResponse.class);


    @Before
    public void setup() {
        when(authenticator.authenticate(eq("username1"), anyString())).thenReturn(Optional.<Principal>absent());
        when(authenticator.authenticate("username1", "good-password")).thenReturn(Optional.of(new Principal("username1")));
        when(userEncoder.noUser()).thenReturn(loginFailedResponse);
        when(userEncoder.encode(("username1"))).thenReturn(loginSuccessResponse);
    }

    @Test
    public void testSuccessfulAuthentication() throws Exception {

        GoPluginApiRequest goRequest = mock(GoPluginApiRequest.class);
        when(requestDecoder.decode(goRequest)).thenReturn(Optional.of(new AuthenticationRequest("username1", "good-password")));
        GoPluginApiResponse actualResponse = authenticationHandler.call(goRequest);
        assertThat(actualResponse, is(loginSuccessResponse));
    }

    @Test
    public void testUnsuccessfulAuthentication() throws Exception {
        GoPluginApiRequest goRequest = mock(GoPluginApiRequest.class);
        when(requestDecoder.decode(goRequest)).thenReturn(Optional.of(new AuthenticationRequest("username1", "bad-password")));
        GoPluginApiResponse actualResponse = authenticationHandler.call(goRequest);
        assertThat(actualResponse, is(loginFailedResponse));
    }

}