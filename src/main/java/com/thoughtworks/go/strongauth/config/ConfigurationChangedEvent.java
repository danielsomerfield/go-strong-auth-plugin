package com.thoughtworks.go.strongauth.config;

import lombok.Value;

@Value
public class ConfigurationChangedEvent {
    private final PluginConfiguration pluginConfiguration;
}
