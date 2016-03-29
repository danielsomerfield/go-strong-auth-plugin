package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Optional;

public interface HashProvider {
    Optional<byte[]> buildHash(final String hashConfig, final String password, final PrincipalDetail principalDetail);
    boolean canHandle(String hashConfig);
}
