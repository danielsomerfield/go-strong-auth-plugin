package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.thoughtworks.go.strongauth.authentication.hash.BCryptProvider;
import com.thoughtworks.go.strongauth.authentication.hash.PBESpecHashProvider;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticatorIntegrationTest {

    private PrincipalDetailSource principalDetailSource = mock(PrincipalDetailSource.class);
    private List<? extends HashProvider> hashProviders = ImmutableList.of(
            new PBESpecHashProvider(),
            new BCryptProvider()
    );

    private Authenticator authenticator = new Authenticator(principalDetailSource, hashProviders);

    @Before
    @SneakyThrows
    public void setup() {
        when(principalDetailSource.byUsername(any(String.class))).thenReturn(Optional.<PrincipalDetail>absent());
        when(principalDetailSource.byUsername("username123")).thenReturn(
                Optional.of(new PrincipalDetail(
                        "username123",
                        "21296e93b24064d688c934a6c22462cd5c74b927258c85e20eb1362921d15332",
                        Base64.encodeBase64String("salt".getBytes("UTF-8")),
                        "PBKDF2WithHmacSHA1(10000, 256)"
                )));

        when(principalDetailSource.byUsername("username5Iterations")).thenReturn(
                Optional.of(new PrincipalDetail(
                        "username5Iterations",
                        "21296e93b24064d688c934a6c22462cd5c74b927258c85e20eb1362921d15332",
                        Base64.encodeBase64String("salt".getBytes("UTF-8")),
                        "PBKDF2WithHmacSHA1(5, 256)"
                )));


        when(principalDetailSource.byUsername("usingBCrypt")).thenReturn(
                Optional.of(new PrincipalDetail(
                        "usingBCrypt",
                        "$2a$10$xannybUjnYmLhbfwHsd3deJXBRaBG7.U.t/Jl6gMEHMkod7U/XTs6",
                        "",
                        "bcrypt"
                )));
    }

    @Test
    public void testAuthenticateUserSuccessBCrypt() {
        String username = "usingBCrypt";
        String password = "pazzword123";

        Optional<Principal> maybePrincipal = authenticator.authenticate(username, password);
        assertThat(maybePrincipal, is(Optional.of(new Principal("usingBCrypt"))));
    }

    @Test
    public void testAuthenticateUserFailureBCrypt() {
        String username = "usingBCrypt";
        String password = "pazzword1234";

        Optional<Principal> maybePrincipal = authenticator.authenticate(username, password);
        assertThat(maybePrincipal, is(Optional.<Principal>absent()));
    }

    @Test
    public void testAuthenticateUserSuccessPBKDF2() {
        String username = "username123";
        String password = "pazzword123";

        Optional<Principal> maybePrincipal = authenticator.authenticate(username, password);
        assertThat(maybePrincipal, is(Optional.of(new Principal("username123"))));
    }

    @Test
    public void testAuthenticateNoUserMatch() {
        String username = "noUser";
        String password = "pazzword123";

        Optional<Principal> maybePrincipal = authenticator.authenticate(username, password);
        assertThat(maybePrincipal, is(Optional.<Principal>absent()));
    }

    @Test
    public void testAuthenticateIterationsWrong() {
        String username = "username5Iterations";
        String password = "pazzword123";

        Optional<Principal> maybePrincipal = authenticator.authenticate(username, password);
        assertThat(maybePrincipal, is(Optional.<Principal>absent()));
    }

    @Test
    public void testAuthenticateNoPasswordMatch() {
        String username = "username123";
        String password = "noMatch";

        Optional<Principal> maybePrincipal = authenticator.authenticate(username, password);
        assertThat(maybePrincipal, is(Optional.<Principal>absent()));
    }

}
