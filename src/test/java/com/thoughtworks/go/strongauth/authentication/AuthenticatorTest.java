package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Optional;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticatorTest {

    private PrincipalDetailSource principalDetailSource = mock(PrincipalDetailSource.class);
    private Authenticator authenticator = new Authenticator(principalDetailSource);

    @Test
    @Ignore
    public void testAuthenticateUserSuccess() {
        String username = "username123";
        String password = "pazzword123";

        when(principalDetailSource.byUsername("username123")).thenReturn(
                Optional.of(new PrincipalDetail(
                        "username123", "salt", "21296e93b24064d688c934a6c22462cd5c74b927258c85e20eb1362921d15332", "PBKDF2WithHmacSHA1(10000, 256)"
                )));

        Optional<Principal> maybePrincipal = authenticator.authenticate(username, password);
        assertThat(maybePrincipal, is(Optional.of(new Principal("username123"))));
    }



}
