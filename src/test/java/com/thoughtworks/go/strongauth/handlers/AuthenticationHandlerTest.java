package com.thoughtworks.go.strongauth.handlers;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;
import com.thoughtworks.go.strongauth.authentication.Authenticator;
import com.thoughtworks.go.strongauth.authentication.Principal;
import com.thoughtworks.go.strongauth.goAPI.GoUser;
import com.thoughtworks.go.strongauth.goAPI.GoUserAPI;
import com.thoughtworks.go.strongauth.wire.GoAuthenticationRequestDecoder;
import com.thoughtworks.go.strongauth.wire.AuthenticationResponseEncoder;
import com.thoughtworks.go.strongauth.wire.RedirectResponseEncoder;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class AuthenticationHandlerTest {

    private GoAuthenticationRequestDecoder requestDecoder = mock(GoAuthenticationRequestDecoder.class);
    private Authenticator authenticator = mock(Authenticator.class);
    private RedirectResponseEncoder redirectResponseEncoder = mock(RedirectResponseEncoder.class);
    private GoUserAPI goUserAPI = mock(GoUserAPI.class);
    private AuthenticationHandler authenticationHandler = new AuthenticationHandler(
            requestDecoder,
            authenticator,
            goUserAPI,
            redirectResponseEncoder
    );

    GoPluginApiResponse loginSuccessResponse = mock(GoPluginApiResponse.class);
    GoPluginApiResponse loginPageResponse = mock(GoPluginApiResponse.class);

    @Before
    public void setup() {
        when(authenticator.authenticate(eq("username1"), anyString())).thenReturn(Optional.<Principal>absent());
        when(authenticator.authenticate("username1", "good-password")).thenReturn(Optional.of(new Principal("username1")));
        when(redirectResponseEncoder.redirectToServerBase()).thenReturn(loginSuccessResponse);
        when(redirectResponseEncoder.redirectToLoginPage()).thenReturn(loginPageResponse);
    }

    @Test
    public void testSuccessfulAuthentication() throws Exception {

        GoPluginApiRequest goRequest = mock(GoPluginApiRequest.class);
        when(requestDecoder.decode(goRequest)).thenReturn(new AuthenticationRequest("username1", "good-password"));
        GoPluginApiResponse actualResponse = authenticationHandler.call(goRequest);
        assertThat(actualResponse, is(loginSuccessResponse));
        verify(goUserAPI).authenticateUser(new GoUser("username1"));
    }

    @Test
    public void testUnsuccessfulAuthentication() throws Exception {
        GoPluginApiRequest goRequest = mock(GoPluginApiRequest.class);
        when(requestDecoder.decode(goRequest)).thenReturn(new AuthenticationRequest("username1", "bad-password"));
        GoPluginApiResponse actualResponse = authenticationHandler.call(goRequest);
        assertThat(actualResponse, is(loginPageResponse));
        verify(goUserAPI, never()).authenticateUser(any(GoUser.class));
    }

}