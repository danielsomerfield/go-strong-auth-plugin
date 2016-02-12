package com.thoughtworks.go.strongauth.wire;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;

public interface GoAuthenticationRequestDecoder {

    AuthenticationRequest decode(GoPluginApiRequest goRequest);

}
