package com.thoughtworks.go.strongauth.util.io;

import com.thoughtworks.go.strongauth.util.InputStreamSource;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FileChangeMonitor implements InputStreamSource<File> {

    private boolean running = true;
    private final List<SourceChangerListener> sourceChangerListeners = new LinkedList<>();
    private final static ExecutorService executorService = Executors.newCachedThreadPool();
    private final File file;

    @SneakyThrows(IOException.class)
    public FileChangeMonitor(final File file) {

        this.file = file;
        final Path parentDir = FileSystems.getDefault().getPath(file.getAbsolutePath()).getParent();
        final WatchService watchService = FileSystems.getDefault().newWatchService();
        parentDir.register(watchService, ENTRY_MODIFY);

        executorService.submit(new Runnable() {
            @Override
            @SneakyThrows(IOException.class)
            public void run() {
                while (running) {
                    final WatchKey key;
                    try {
                        key = watchService.take();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    for (WatchEvent evt : key.pollEvents()) {
                        if (evt.kind() == ENTRY_MODIFY) {
                            notifyListeners(new SourceChangeEvent(new File(parentDir.toString(), evt.context().toString())));
                        }
                    }
                }
                watchService.close();

            }
        }, "Watcher thread");

    }

    public void stop() {
        running = false;
    }

    @Override
    public void addChangeListener(SourceChangerListener<File> sourceChangerListener) {
        synchronized (sourceChangerListeners) {
            if (!sourceChangerListeners.contains(sourceChangerListener)) {
                sourceChangerListeners.add(sourceChangerListener);
            }
        }
    }

    private void notifyListeners(SourceChangeEvent sourceChangeEvent) {
        SourceChangerListener[] listeners;
        synchronized (sourceChangerListeners) {
            listeners = new SourceChangerListener[sourceChangerListeners.size()];
            sourceChangerListeners.toArray(listeners);
        }

        for (SourceChangerListener listener : listeners) {
            listener.sourceChanged(sourceChangeEvent);
        }
    }

    @Override
    public InputStream inputStream() throws IOException {
        return new FileInputStream(file);
    }
}
