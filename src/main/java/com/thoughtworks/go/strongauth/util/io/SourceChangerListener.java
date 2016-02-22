package com.thoughtworks.go.strongauth.util.io;

public interface SourceChangerListener<T> {
    void sourceChanged(final SourceChangeEvent<T> event);
}
