package com.thoughtworks.go.strongauth.config;

import lombok.Value;

import java.io.File;

@Value
public class PluginConfiguration {

    public static final String DEFAULT_SOURCE_FILE_PATH = "/etc/go/passwd";
    private final File principalSourceFile;

    public PluginConfiguration() {
        this(DEFAULT_SOURCE_FILE_PATH);
    }

    public PluginConfiguration(final String principalSourceFilePath) {
        this.principalSourceFile = principalSourceFilePath != null ?
                new File(principalSourceFilePath) :
                new File(DEFAULT_SOURCE_FILE_PATH);
    }

    @Override
    public String toString() {
        return "PluginConfiguration{" +
                "principalSourceFilePath='" + principalSourceFile + '\'' +
                '}';
    }
}