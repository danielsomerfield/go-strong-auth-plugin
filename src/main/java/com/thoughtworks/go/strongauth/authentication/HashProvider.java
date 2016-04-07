package com.thoughtworks.go.strongauth.authentication;

public interface HashProvider {
    boolean validateHash(final String password, final PrincipalDetail principalDetail);

    boolean canHandle(String hashConfig);
}
