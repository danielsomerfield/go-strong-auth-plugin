package com.thoughtworks.go.strongauth.glue;

import com.thoughtworks.go.TestHelpers;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class StrongAuthGlue {

    private HttpResponse response;
    private TestHelpers testHelpers = new TestHelpers();
    private CookieStore cookieStore = new BasicCookieStore();
    private CloseableHttpClient httpClient = HttpClientBuilder.create()
            .setDefaultCookieStore(cookieStore)
            .setRedirectStrategy(new DefaultRedirectStrategy() {
                @Override
                public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                    return false;
                }
            })
            .setDefaultRequestConfig(RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build())
            .build();

    @Given("^Go CD is running$")
    public void goCDIsRunning() throws Throwable {
        pingGo();
    }

    @Given("^Auth is disabled$")
    public void authIsDisabled() throws Throwable {
        testHelpers.disableAuth();
    }

    private void pingGo() throws IOException {
        HttpGet get = new HttpGet(getURLForPath("/"));
        httpClient().execute(get);
    }

    @Then("^I see the go home page$")
    public void iSeeTheGoHomePage() throws Throwable {
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_OK));
    }

    @When("^I make a request to the go home page$")
    public void iMakeARequestToTheGoHomePage() throws Throwable {
        HttpGet get = new HttpGet(getURLForPath(("/go/admin/pipeline/new?group=defaultGroup")));
        CloseableHttpClient client = httpClient();
        response = client.execute(get);
    }

    private String getURLForPath(final String path) {
        return format("http://%s:%s%s", getGoHost(), getPort(), path);
    }

    private String getGoHost() {
        return System.getProperty("GO_CD_HOST", "172.17.0.2");
    }

    private String getPort() {
        return "8153";
    }

    private CloseableHttpClient httpClient() {
        return httpClient;
    }

    @Given("^Auth is enabled$")
    public void authIsEnabled() throws Throwable {
        testHelpers.enableAuth();
    }

    @Then("^I am redirected to the login screen$")
    public void iAmRedirectedToTheLoginScreen() throws Throwable {
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_MOVED_TEMPORARILY));
        assertThat(response.getFirstHeader("Location"), is(notNullValue()));
        final String host = System.getProperty("GO_CD_HOST", "172.17.0.2");
        assertThat(response.getFirstHeader("Location").getValue(), is(format("http://%s:8153/go/auth/login", host)));
    }

    @Given("^There are no existing users$")
    public void thereAreNoExistingUsers() throws Throwable {
        testHelpers.clearAuthFile();
    }

    @Given("^A login exists for \"([^\"]*)\", \"([^\"]*)\"$")
    public void aLoginExistsFor(String username, String password) throws Throwable {
        testHelpers.clearAuthFile();
        testHelpers.createPasswordEntryFor(username, password);
        Thread.sleep(3000); //Can we find a better way?
    }

    @When("^I login$")
    public void iLogin() throws Throwable {
        HttpPost post = new HttpPost(getURLForPath("/go/auth/security_check"));
        post.setHeader("Content-type", "application/x-www-form-urlencoded");
        post.setEntity(new StringEntity("j_username=aUser&j_password=aSecret"));
        final CloseableHttpResponse response = httpClient().execute(post);
        assertThat(response.getStatusLine().getStatusCode(), is(302));
    }
}
