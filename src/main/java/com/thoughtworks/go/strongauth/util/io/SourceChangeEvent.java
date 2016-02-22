package com.thoughtworks.go.strongauth.util.io;

import lombok.Value;

@Value
public class SourceChangeEvent<T> {
    private final T file;
}
