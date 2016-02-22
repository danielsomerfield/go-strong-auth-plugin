package com.thoughtworks.go.strongauth.util;

import com.thoughtworks.go.strongauth.util.io.SourceChangerListener;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamSource<T> {

    void addChangeListener(SourceChangerListener sourceChangerListener);

    InputStream inputStream() throws IOException;
}
