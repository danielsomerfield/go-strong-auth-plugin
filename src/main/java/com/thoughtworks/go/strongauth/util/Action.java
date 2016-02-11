package com.thoughtworks.go.strongauth.util;

public interface Action<InputType, OutputType> {
    OutputType call(InputType request);
}
