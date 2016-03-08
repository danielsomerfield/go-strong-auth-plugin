package com.thoughtworks.go.strongauth.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.strongauth.util.ChangeMonitor;
import com.thoughtworks.go.strongauth.util.ChangeMonitor.ChangeMonitorDelegate;

public class ConfigurationMonitor implements ChangeMonitorDelegate<ConfigurationChangedEvent, PluginConfiguration> {

    private final GoAPI goAPI;
    private ChangeMonitor<ConfigurationChangedEvent, PluginConfiguration> changeMonitor;

    public ConfigurationMonitor(
            final GoAPI goAPI
    ) {
        this.goAPI = goAPI;
        this.changeMonitor = new ChangeMonitor<>(this);
    }

    public void start() {
        this.changeMonitor.start();
    }

    public void addConfigurationChangeListener(ConfigurationChangeListener listener) {
        this.changeMonitor.addChangeListener(listener);
    }

    @Override
    public ConfigurationChangedEvent createNotifyEvent(Optional<PluginConfiguration> newMaybeValue, Optional<PluginConfiguration> oldMaybeValue) {
        return new ConfigurationChangedEvent(newMaybeValue);
    }

    @Override
    public Optional<PluginConfiguration> newValue() {
        return currentConfiguration();
    }

    public Optional<PluginConfiguration> currentConfiguration() {
        return Optional.of(goAPI.getPluginConfiguration());
    }
}
