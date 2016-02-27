package com.thoughtworks.go.strongauth.config;

import com.google.common.base.Optional;

public interface StrongAuthPluginConfigurationProvider {
    Optional<String> getPasswordFileLocation();
}
