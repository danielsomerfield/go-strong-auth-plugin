package com.thoughtworks.go.strongauth;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.junit.Ignore;
import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class E2ETest {

    public String getGoHost() {
        return "192.168.99.100";
    }

    @Test
    public void testLoginSucceedsWithoutAuthConfigured() throws Exception {
        HttpGet get = new HttpGet(format("http://%s:8153/go/admin/pipeline/new?group=defaultGroup", getGoHost()));
        CloseableHttpClient client = httpClient();
        HttpResponse response = client.execute(get);
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_OK));
    }

    @Test
    @Ignore
    public void testRedirectWithAuthConfigured() throws Exception {
        HttpGet get = new HttpGet(format("http://%s:8153/go/admin/pipeline/new?group=defaultGroup", getGoHost()));
        CloseableHttpClient client = httpClient();
        HttpResponse response = client.execute(get);
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_MOVED_TEMPORARILY));
    }

    private CloseableHttpClient httpClient() {
        return HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy(){
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return false;
            }
        }).build();
    }
}
