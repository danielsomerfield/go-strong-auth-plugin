package com.thoughtworks.go.strongauth.wire;

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@Ignore
public class RedirectResponseEncoderTest {


    @Test
    public void testRedirectToServerBase() throws Exception {
        URI serverBase = URI.create("http://serverbase.example.com");
        RedirectResponseEncoder redirectResponseEncoder = new RedirectResponseEncoder(serverBase);
        GoPluginApiResponse goPluginApiResponse = redirectResponseEncoder.redirectToServerBase();
        assertThat(goPluginApiResponse.responseHeaders().get("Location"), is("http://serverbase.example.com/"));
        assertThat(goPluginApiResponse.responseCode(), is(302));
        assertThat(goPluginApiResponse.responseBody(), is(nullValue()));
    }

    @Test
    public void testRedirectToLoginPage() throws Exception {
        URI serverBase = URI.create("http://serverbase.example.com");
        RedirectResponseEncoder redirectResponseEncoder = new RedirectResponseEncoder(serverBase);
        GoPluginApiResponse goPluginApiResponse = redirectResponseEncoder.redirectToLoginPage();
        assertThat(goPluginApiResponse.responseHeaders().get("Location"), is("http://serverbase.example.com/go/auth/login"));
        assertThat(goPluginApiResponse.responseCode(), is(302));
        assertThat(goPluginApiResponse.responseBody(), is(nullValue()));
    }
}