package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Optional;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class AuthenticatorTest {

    private PrincipalSource principalSource = mock(PrincipalSource.class);
    private Authenticator authenticator = new Authenticator(principalSource);

    @Test
    public void testAuthenticateUserSuccess() {
        String username = "asdfasd";
        String password = "ffdsasdf";

        Authenticator authenticator = new Authenticator(principalSource);
        Optional<Principal> principal = authenticator.authenticate(username, password);
//        assertThat(principal, is());
    }

}
