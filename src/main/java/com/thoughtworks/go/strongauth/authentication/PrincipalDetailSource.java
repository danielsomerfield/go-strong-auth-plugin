package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Optional;

public interface PrincipalDetailSource {
    Optional<PrincipalDetail> byUsername(String username);
}
