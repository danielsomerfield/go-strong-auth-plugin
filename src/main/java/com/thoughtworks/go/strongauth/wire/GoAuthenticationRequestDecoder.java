package com.thoughtworks.go.strongauth.wire;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;

public class GoAuthenticationRequestDecoder {

    public Optional<AuthenticationRequest> decode(GoPluginApiRequest goRequest) {
        throw new UnsupportedOperationException("NYI");
    }

}
