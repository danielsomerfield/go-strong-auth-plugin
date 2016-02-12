package com.thoughtworks.go.strongauth.glue;

import com.thoughtworks.go.TestHelpers;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
        return "192.168.99.100";
    }

    private String getPort() {
        return "8153";
    }

    private CloseableHttpClient httpClient() {
        return HttpClientBuilder.create().setDefaultCookieStore(cookieStore).setRedirectStrategy(new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return false;
            }
        }).build();
    }

    @Given("^Auth is enabled$")
    public void authIsEnabled() throws Throwable {
        testHelpers.enableAuth();
    }

    @Then("^I am redirected to the login screen$")
    public void iAmRedirectedToTheLoginScreen() throws Throwable {
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_MOVED_TEMPORARILY));
        assertThat(response.getFirstHeader("Location"), is(notNullValue()));
        assertThat(response.getFirstHeader("Location").getValue(), is("http://192.168.99.100:8153/go/auth/login"));
    }

    @Given("^There are no existing users$")
    public void thereAreNoExistingUsers() throws Throwable {
        testHelpers.clearAuthFile();
    }

    @Given("^A login exists for \"([^\"]*)\", \"([^\"]*)\"$")
    public void aLoginExistsFor(String username, String password) throws Throwable {
        testHelpers.clearAuthFile();
        testHelpers.createPasswordEntryFor(username, password);
    }

    @When("^I login$")
    public void iLogin() throws Throwable {
        HttpPost post = new HttpPost(getURLForPath("go/auth/security_check"));
        httpClient().execute(post);
    }
}
