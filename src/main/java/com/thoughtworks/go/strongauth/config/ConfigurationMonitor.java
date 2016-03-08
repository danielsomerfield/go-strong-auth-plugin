package com.thoughtworks.go.strongauth.config;

import com.google.common.base.Optional;
import com.thoughtworks.go.strongauth.util.ChangeMonitor;
import com.thoughtworks.go.strongauth.util.ChangeMonitor.ChangeMonitorDelegate;

import java.util.List;

public class ConfigurationMonitor implements ChangeMonitorDelegate<ConfigurationChangedEvent, PluginConfiguration> {

    private final GoAPI goAPI;
    private ChangeMonitor<ConfigurationChangedEvent, PluginConfiguration> changeMonitor;

    public ConfigurationMonitor(
            final GoAPI goAPI,
            final List<? extends ConfigurationChangeListener> configurationChangeListeners
    ) {
        this.goAPI = goAPI;
        this.changeMonitor = new ChangeMonitor<>(this, configurationChangeListeners);
    }

    @Override
    public ConfigurationChangedEvent createNotifyEvent(Optional<PluginConfiguration> newMaybeValue, Optional<PluginConfiguration> oldMaybeValue) {
        return new ConfigurationChangedEvent(newMaybeValue);
    }

    @Override
    public Optional<PluginConfiguration> newValue() {
        return Optional.of(goAPI.getPluginConfiguration());
    }
}
