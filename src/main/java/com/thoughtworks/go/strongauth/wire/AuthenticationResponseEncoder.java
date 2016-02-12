package com.thoughtworks.go.strongauth.wire;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.authentication.Principal;

public interface AuthenticationResponseEncoder {

    GoPluginApiResponse encode(Optional<Principal> principal);
}
