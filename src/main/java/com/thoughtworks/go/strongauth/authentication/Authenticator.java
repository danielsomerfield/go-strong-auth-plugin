package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.util.Functional;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class Authenticator {

    private static final Logger LOGGER = Logger.getLoggerFor(Authenticator.class);

    public Authenticator(PrincipalDetailSource principalDetailSource) {
        this.principalDetailSource = principalDetailSource;
    }
    private PrincipalDetailSource principalDetailSource;
    public final String pluginId = Constants.PLUGIN_ID;

    public Optional<Principal> authenticate(final String username, final String password) {
        return Functional.flatMap(principalDetailSource.byUsername(username), new Function<PrincipalDetail, Optional<Principal>>() {
            @Override
            public Optional<Principal> apply(PrincipalDetail principalDetail) {
                return toPrincipal(principalDetail, password);
            }
        });
    }

    private Optional<Principal> toPrincipal(final PrincipalDetail principalDetail, final String password) {

        Optional<HashConfig> maybeHashConfig = parseHashConfig(principalDetail.getHashConfiguration());
        return Functional.flatMap(maybeHashConfig, new Function<HashConfig, Optional<Principal>>() {

            @Override
            @SneakyThrows(UnsupportedEncodingException.class)
            public Optional<Principal> apply(HashConfig hashConfig) {
                PBEKeySpec spec = new PBEKeySpec(
                        password.toCharArray(),
                        principalDetail.getSalt().getBytes("UTF-8"),
                        hashConfig.getIterations(),
                        hashConfig.getKeySize());

                try {
                    byte[] key = SecretKeyFactory.getInstance(hashConfig.getAlgorithm()).generateSecret(spec).getEncoded();
                    return Arrays.equals(Hex.decodeHex(principalDetail.getPasswordHash().toCharArray()), key) ?
                            Optional.of(new Principal(principalDetail.getUsername())) :
                            Optional.<Principal>absent();

                } catch (DecoderException | GeneralSecurityException e) {
                    LOGGER.warn("Failed to read principal entry.", e);
                    return Optional.absent();
                }
            }
        });

    }

    @Value
    private static class HashConfig {
        private final String algorithm;
        private final int iterations;
        private final int keySize;
    }

    private static final Pattern HASH_CONFIG_PATTERN = Pattern.compile("^(\\w+)\\((\\d+), (\\d+)\\)$");

    private static Optional<HashConfig> parseHashConfig(String configString) {
        final Matcher matcher = HASH_CONFIG_PATTERN.matcher(configString);
        if (matcher.find()) {
            return Optional.of(new HashConfig(
                    matcher.group(1),
                    parseInt(matcher.group(2)),
                    parseInt(matcher.group(3)))
            );
        } else {
            return Optional.absent();
        }
    }
}
