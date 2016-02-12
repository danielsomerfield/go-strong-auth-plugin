package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Optional;

public class Authenticator {

    public Authenticator(PrincipalDetailSource principalDetailSource) {

    }

    public Optional<Principal> authenticate(String username, String password) {
        return Optional.absent();
    }
}
