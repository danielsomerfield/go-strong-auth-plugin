package com.thoughtworks.go.strongauth.util.io;

import com.google.common.base.Optional;
import com.thoughtworks.go.strongauth.util.ChangeMonitor;
import com.thoughtworks.go.strongauth.util.ChangeMonitor.ChangeMonitorDelegate;
import com.thoughtworks.go.strongauth.util.ChangeMonitor.MonitorListener;
import com.thoughtworks.go.strongauth.util.InputStreamSource;

import java.io.*;

import static java.lang.String.format;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class FileChangeMonitor implements InputStreamSource, ChangeMonitorDelegate<SourceChangeEvent, String> {

    private final File file;

    private ChangeMonitor<SourceChangeEvent, String> changeMonitor;

    public FileChangeMonitor(final File file) {
        this.file = file;
        this.changeMonitor = new ChangeMonitor<>(this);
    }

    public SourceChangeEvent createNotifyEvent(Optional<String> newMaybeValue, Optional<String> oldMaybeValue) {
        return new SourceChangeEvent(contents(), file.getAbsolutePath());
    }

    private InputStream contents() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return new ByteArrayInputStream(new byte[0]);
        }
    }

    public Optional<String> newValue() {
        try {
            return Optional.of(md5Hex(new FileInputStream(file)));
        } catch (IOException e) {
            return Optional.absent();
        }
    }

    public void start() {
        this.changeMonitor.start();
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
