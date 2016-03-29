package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.util.Functional;
import lombok.Value;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thoughtworks.util.Functional.flatMap;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class Authenticator {

    public final String pluginId = Constants.PLUGIN_ID;
    private static final Logger LOGGER = Logger.getLoggerFor(Authenticator.class);

    public Authenticator(PrincipalDetailSource principalDetailSource) {
        this.principalDetailSource = principalDetailSource;
    }

    private PrincipalDetailSource principalDetailSource;

    public Optional<Principal> authenticate(final String username, final String password) {
        LOGGER.info(format("Login attempt for user %s", username));
        Optional<Principal> maybePrincipal = flatMap(principalDetailSource.byUsername(username), new Function<PrincipalDetail, Optional<Principal>>() {
            @Override
            public Optional<Principal> apply(PrincipalDetail principalDetail) {
                return toPrincipal(principalDetail, password);
            }
        });

        if (!maybePrincipal.isPresent()) {
            LOGGER.warn(format("Login attempt for user %s failed.", username));
        }
        return maybePrincipal;
    }

    private Optional<Principal> toPrincipal(final PrincipalDetail principalDetail, final String password) {

        Optional<HashConfig> maybeHashConfig = parseHashConfig(principalDetail.getHashConfiguration());
        return flatMap(maybeHashConfig, new Function<HashConfig, Optional<Principal>>() {

            @Override
            public Optional<Principal> apply(HashConfig hashConfig) {
                try {
                    final Optional<byte[]> maybeKey = createHashBytes(hashConfig, password, principalDetail);
                    return flatMap(maybeKey, new Function<byte[], Optional<Principal>>() {
                        @Override
                        public Optional<Principal> apply(byte[] key) {
                            try {
                                return (Arrays.equals(Hex.decodeHex(principalDetail.getPasswordHash().toCharArray()), key) ?
                                        Optional.of(new Principal(principalDetail.getUsername())) :
                                        Optional.<Principal>absent());
                            } catch (DecoderException e) {
                                return Optional.absent();
                            }
                        }
                    });
                } catch (GeneralSecurityException e) {
                    LOGGER.warn("Failed to read principal entry.", e);
                    return Optional.absent();
                }
            }
        });

    }

    private Optional<byte[]> createHashBytes(final HashConfig hashConfig, final String password, final PrincipalDetail principalDetail) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Optional<HashProvider> maybeHashFactory = getHashProvider(hashConfig.algorithm);
        return flatMap(maybeHashFactory, new Function<HashProvider, Optional<byte[]>>() {
            @Override
            public Optional<byte[]> apply(HashProvider input) {
                return input.buildHash(hashConfig, password, principalDetail);
            }
        });
    }

    private Optional<HashProvider> getHashProvider(final String algorithm) {
        return Optional.of(new HashProvider());
    }

    private static class HashProvider {
        public Optional<byte []> buildHash(final HashConfig hashConfig, final String password, final PrincipalDetail principalDetail) {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    Base64.decodeBase64(principalDetail.getSalt()),
                    hashConfig.getIterations(),
                    hashConfig.getKeySize());
            try {
                return Optional.of(SecretKeyFactory.getInstance(hashConfig.getAlgorithm()).generateSecret(spec).getEncoded());
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                LOGGER.warn("build hash", e);
                return Optional.absent();
            }
        }
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
