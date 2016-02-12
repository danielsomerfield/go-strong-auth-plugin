package com.thoughtworks.go.strongauth.authentication;

import lombok.Value;

@Value
public class PrincipalDetail {
    private final String username;
    private final String passwordHash;
    private final String salt;
    private final String hashConfiguration;
}
