package com.thoughtworks.go.strongauth.glue;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StrongAuthGlue {

    private HttpResponse response;

    @Given("^Go CD is running$")
    public void goCDIsRunning() throws Throwable {
        pingGo();
    }

    @Given("^Auth is disabled$")
    public void authIsDisabled() throws Throwable {

    }

    private void pingGo() throws IOException {
        HttpGet get = new HttpGet(format("http://%s:8153/", getGoHost()));
        httpClient().execute(get);
    }

    @Then("^I see the go home page$")
    public void iSeeTheGoHomePage() throws Throwable {
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_OK));
    }

    @When("^I make a request to the go home page$")
    public void iMakeARequestToTheGoHomePage() throws Throwable {
        HttpGet get = new HttpGet(format("http://%s:8153/go/admin/pipeline/new?group=defaultGroup", getGoHost()));
        CloseableHttpClient client = httpClient();
        response = client.execute(get);

    }

    public String getGoHost() {
        return "192.168.99.100";
    }


    private CloseableHttpClient httpClient() {
        return HttpClientBuilder.create().setRedirectStrategy(new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return false;
            }
        }).build();
    }


    //    public void testRedirectWithAuthConfigured() throws Exception {
//        HttpGet get = new HttpGet(format("http://%s:8153/go/admin/pipeline/new?group=defaultGroup", getGoHost()));
//        CloseableHttpClient client = httpClient();
//        HttpResponse response = client.execute(get);
//        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_MOVED_TEMPORARILY));
//    }
}
