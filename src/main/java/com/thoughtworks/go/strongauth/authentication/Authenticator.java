package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.util.Constants;
import org.apache.commons.codec.DecoderException;

import java.util.List;

import static com.thoughtworks.util.Functional.flatMap;
import static java.lang.String.format;

public class Authenticator {

    public final String pluginId = Constants.PLUGIN_ID;
    private static final Logger LOGGER = Logger.getLoggerFor(Authenticator.class);
    private final List<? extends HashProvider> hashProviders;

    public Authenticator(
            final PrincipalDetailSource principalDetailSource,
            final List<? extends HashProvider> hashProviders
    ) {
        this.principalDetailSource = principalDetailSource;
        this.hashProviders = hashProviders;
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
        Optional<? extends HashProvider> maybeHashProvider = getHashProvider(principalDetail.getHashConfiguration());

        return flatMap(
                maybeHashProvider,
                new Function<HashProvider, Optional<Principal>>() {
                    @Override
                    public Optional<Principal> apply(HashProvider hashProvider) {
                        return hashProvider.validateHash(password, principalDetail) ?
                                Optional.of(new Principal(principalDetail.getUsername())) :
                                Optional.<Principal>absent();
                    }
                }
        );
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
