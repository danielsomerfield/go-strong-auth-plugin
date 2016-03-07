package com.thoughtworks.go.strongauth.util;

import com.thoughtworks.go.strongauth.util.io.SourceChangeListener;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamSource {

    void addChangeListener(SourceChangeListener sourceChangeListener);

    InputStream inputStream() throws IOException;
}
