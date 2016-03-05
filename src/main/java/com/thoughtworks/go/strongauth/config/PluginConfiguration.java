package com.thoughtworks.go.strongauth.config;

import java.io.File;

public class PluginConfiguration {

    public File principalSourceFile() {
        return new File("/etc/go/passwd");
    }
}