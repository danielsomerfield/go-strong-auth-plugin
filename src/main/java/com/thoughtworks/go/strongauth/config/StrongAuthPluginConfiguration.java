package com.thoughtworks.go.strongauth.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import java.io.File;

public class StrongAuthPluginConfiguration {
    private final ImmutableList<StrongAuthPluginConfigurationProvider> configurationProviders;

    public StrongAuthPluginConfiguration(StrongAuthPluginConfigurationProvider... configurationProviders) {
        this.configurationProviders = ImmutableList.copyOf(configurationProviders);
    }

    public Optional<File> getPasswordFileLocation() {
        for (StrongAuthPluginConfigurationProvider provider : configurationProviders) {
            Optional<String> location = provider.getPasswordFileLocation();
            if (location.isPresent()) {
                return location.transform(new Function<String, File>() {
                    @Override
                    public File apply(String filePath) {
                        return new File(filePath);
                    }
                });
            }
        }
        return Optional.absent();
    }
}
