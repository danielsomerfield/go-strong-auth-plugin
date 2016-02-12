package com.thoughtworks.go.strongauth.authentication;

import lombok.Value;

@Value
public class AuthenticationRequest {
    private final String username;
    private final String password;
}
