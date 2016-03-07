package com.thoughtworks.go.strongauth.util.io;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.util.ChangeMonitor;
import com.thoughtworks.go.strongauth.util.ChangeMonitor.FileChangeDelegate;
import com.thoughtworks.go.strongauth.util.ChangeMonitor.MonitorListener;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.go.strongauth.util.InputStreamSource;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.lang.String.format;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class FileChangeMonitor implements InputStreamSource, FileChangeDelegate<SourceChangeEvent> {

    private final File file;

    private ChangeMonitor<SourceChangeEvent> changeMonitor;

    public FileChangeMonitor(final File file) {
        this.file = file;
        this.changeMonitor = new ChangeMonitor<>(this);
    }

    public SourceChangeEvent createNotifyEvent(Optional<?> newMaybeValue, Optional<?> oldMaybeValue) {
        return new SourceChangeEvent(contents(), file.getAbsolutePath());
    }

    private InputStream contents() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    public boolean valueChanged(Optional<?> newMaybeHash, Optional<?> maybeHash) {
        return !newMaybeHash.equals(maybeHash);
    }

    public Optional<?> newValue() {
        try {
            return Optional.of(md5Hex(new FileInputStream(file)));
        } catch (IOException e) {
            return Optional.absent();
        }
    }

    public void stop() {
        this.changeMonitor.stop();
    }

    @Override
    public void addChangeListener(final SourceChangeListener sourceChangeListener) {
        this.changeMonitor.addChangeListener(new MonitorListener<SourceChangeEvent>() {
            @Override
            public void changed(SourceChangeEvent event) {
                sourceChangeListener.sourceChanged(event);
            }
        });
    }

    @Override
    public InputStream inputStream() throws IOException {
        return new FileInputStream(file);
    }
}
