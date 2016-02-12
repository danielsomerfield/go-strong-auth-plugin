package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Optional;

public class Authenticator {

    public Authenticator(PrincipalSource principalSource) {

    }

    public Optional<Principal> authenticate(String username, String password) {
        return null;
    }
}
