package com.thoughtworks.go.strongauth.config;

import com.google.common.base.Optional;

public class DefaultStrongAuthPluginConfigurationProvider implements StrongAuthPluginConfigurationProvider {
    @Override
    public Optional<String> getPasswordFileLocation() {
        return Optional.of("/etc/go/password");
    }
}
