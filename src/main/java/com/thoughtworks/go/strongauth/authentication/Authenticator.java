package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.authentication.hash.PBESpecHashProvider;
import com.thoughtworks.go.strongauth.util.Constants;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;
import java.util.List;

import static com.thoughtworks.util.Functional.flatMap;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class Authenticator {

    public final String pluginId = Constants.PLUGIN_ID;
    private static final Logger LOGGER = Logger.getLoggerFor(Authenticator.class);
    private List<? extends HashProvider> hashProviders = ImmutableList.of(new PBESpecHashProvider());

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
        final Optional<? extends byte[]> maybeHashBytes = createHashBytes(principalDetail.getHashConfiguration(), password, principalDetail);
        return flatMap(
                maybeHashBytes,
                new Function<byte[], Optional<Principal>>() {
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
                }
        );
    }

    private Optional<byte[]> createHashBytes(final String hashConfig, final String password,
                                             final PrincipalDetail principalDetail) {
        Optional<? extends HashProvider> maybeHashFactory = getHashProvider(hashConfig);
        return flatMap(maybeHashFactory, new Function<HashProvider, Optional<byte[]>>() {
            @Override
            public Optional<byte[]> apply(HashProvider hashProvider) {
                return hashProvider.buildHash(hashConfig, password, principalDetail);
            }
        });
    }

    private Optional<? extends HashProvider> getHashProvider(final String hashConfig) {
        for (HashProvider provider : hashProviders) {
            if (provider.canHandle(hashConfig)) {
                return Optional.of(provider);
            }
        }
        return Optional.absent();
    }


}
