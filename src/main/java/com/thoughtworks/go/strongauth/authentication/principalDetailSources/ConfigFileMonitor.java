package com.thoughtworks.go.strongauth.authentication.principalDetailSources;

import com.google.common.base.Optional;
import com.thoughtworks.go.strongauth.config.ConfigurationChangeListener;
import com.thoughtworks.go.strongauth.config.ConfigurationChangedEvent;
import com.thoughtworks.go.strongauth.config.ConfigurationMonitor;
import com.thoughtworks.go.strongauth.config.PluginConfiguration;
import com.thoughtworks.go.strongauth.util.InputStreamSource;
import com.thoughtworks.go.strongauth.util.io.FileChangeMonitor;
import com.thoughtworks.go.strongauth.util.io.SourceChangeListener;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ConfigFileMonitor implements InputStreamSource {

    private final ConfigurationMonitor configurationMonitor;
    private Optional<FileChangeMonitor> maybeFileChangeMonitor = Optional.absent();
    private SourceChangeListener sourceChangeListener;

    public ConfigFileMonitor(
            ConfigurationMonitor configurationMonitor

    ) {
        this.configurationMonitor = configurationMonitor;
        this.configurationMonitor.addConfigurationChangeListener(new ConfigurationChangeListener() {
            @Override
            public void changed(ConfigurationChangedEvent event) {
                final Optional<PluginConfiguration> maybePluginConfiguration = event.getPluginConfiguration();
                if (maybePluginConfiguration.isPresent()) {
                    registerMonitor(maybePluginConfiguration.get().getPrincipalSourceFile());
                }
            }
        });
        this.configurationMonitor.start();
    }

    private void registerMonitor(final File file) {
        if (maybeFileChangeMonitor.isPresent()) {
            maybeFileChangeMonitor.get().stop();
        }
        final FileChangeMonitor changeMonitor = new FileChangeMonitor(file);
        this.maybeFileChangeMonitor = Optional.of(changeMonitor);
        changeMonitor.addChangeListener(sourceChangeListener);
        changeMonitor.start();
    }

    @Override
    public void addChangeListener(SourceChangeListener sourceChangeListener) {
        this.sourceChangeListener = sourceChangeListener;
    }

    @Override
    public InputStream inputStream() throws IOException {
        return maybeFileChangeMonitor.isPresent() ? maybeFileChangeMonitor.get().inputStream() : new ByteArrayInputStream(new byte[0]);
    }
}
