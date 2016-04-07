package com.thoughtworks.go.strongauth.authentication.hash;

import com.thoughtworks.go.strongauth.authentication.HashProvider;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetail;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptProvider implements HashProvider {
    @Override
    public boolean validateHash(String password, PrincipalDetail principalDetail) {
        return BCrypt.checkpw(password, principalDetail.getPasswordHash());
    }

    @Override
    public boolean canHandle(String hashConfig) {
        return hashConfig.equals("bcrypt");
    }
}
