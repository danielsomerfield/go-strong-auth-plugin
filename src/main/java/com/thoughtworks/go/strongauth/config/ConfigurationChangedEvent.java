package com.thoughtworks.go.strongauth.config;

import com.google.common.base.Optional;
import lombok.Value;

@Value
public class ConfigurationChangedEvent {
    private final Optional<PluginConfiguration> pluginConfiguration;
}
