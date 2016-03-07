package com.thoughtworks.go.strongauth.config;

public interface ConfigurationChangeListener {
    void configurationChanged(ConfigurationChangedEvent event);
}
