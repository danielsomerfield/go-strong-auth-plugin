package com.thoughtworks.go.strongauth.util.io;

import lombok.Value;

import java.io.InputStream;

@Value
public class SourceChangeEvent {

    private InputStream inputStream;


}
