package com.thoughtworks.go.strongauth.util.io;

import lombok.Value;

import java.io.File;

@Value
public class FileChangeEvent {
    private final File file;
}
