package com.thoughtworks.go.strongauth.authentication.hash;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.authentication.HashProvider;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetail;
import com.thoughtworks.go.strongauth.util.Constants;
import lombok.Value;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thoughtworks.util.Functional.flatMap;
import static java.lang.Integer.parseInt;

public class PBESpecHashProvider implements HashProvider {
    public final String pluginId = Constants.PLUGIN_ID;
    private static final Logger LOGGER = Logger.getLoggerFor(PBESpecHashProvider.class);

    private static final Pattern HASH_CONFIG_PATTERN = Pattern.compile("^(\\w+)\\((\\d+), (\\d+)\\)$");

    @Override
    public Optional<byte[]> buildHash(final String hashConfigString, final String password, final PrincipalDetail principalDetail) {
        Optional<HashConfig> maybeHashConfig = parseHashConfig(principalDetail.getHashConfiguration());
        return flatMap(maybeHashConfig, new Function<HashConfig, Optional<byte[]>>() {
            @Override
            public Optional<byte[]> apply(HashConfig hashConfig) {
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
        });
    }

    @Override
    public boolean canHandle(String hashConfig) {
        return parseHashConfig(hashConfig).isPresent();
    }

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

    @Value
    private static class HashConfig {
        private final String algorithm;
        private final int iterations;
        private final int keySize;
    }
}
