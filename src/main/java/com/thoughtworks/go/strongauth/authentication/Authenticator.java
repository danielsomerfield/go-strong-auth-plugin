package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.util.Functional;
import lombok.SneakyThrows;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class Authenticator {

    private PrincipalDetailSource principalDetailSource;

    public Authenticator(PrincipalDetailSource principalDetailSource) {
        this.principalDetailSource = principalDetailSource;
    }

    public Optional<Principal> authenticate(final String username, final String password) {
        return Functional.flatMap(principalDetailSource.byUsername(username), new Function<PrincipalDetail, Optional<Principal>>() {
            @Override
            public Optional<Principal> apply(PrincipalDetail principalDetail) {
                return toPrincipal(principalDetail, password);
            }
        });
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    private static Optional<Principal> toPrincipal(PrincipalDetail principalDetail, String password) {

        String hashConfig = principalDetail.getHashConfiguration();

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                principalDetail.getSalt().getBytes("UTF-8"),
                10000,
                256);

        try {
            byte[] key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).getEncoded();
            return Arrays.equals(Hex.decodeHex(principalDetail.getPasswordHash().toCharArray()), key) ?
                    Optional.of(new Principal(principalDetail.getUsername())) :
                    Optional.<Principal>absent();

        } catch (DecoderException e) {
            //TODO: Log problem
            return Optional.absent();
        } catch (GeneralSecurityException e) {
            //TODO: Log problem
            return Optional.absent();
        }
    }
}
