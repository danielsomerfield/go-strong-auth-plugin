package com.thoughtworks.go.strongauth.config;

import com.google.common.base.Optional;

import java.io.File;

public interface StrongAuthPluginConfigurationProvider {
    Optional<String> getPasswordFileLocation();
}
