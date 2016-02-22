package com.thoughtworks.go.strongauth.util.io;

import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FileChangeMonitor {

    private boolean running = true;
    private final List<FileChangeListener> fileChangeListeners = new LinkedList<>();
    private final static ExecutorService executorService = Executors.newCachedThreadPool();

    @SneakyThrows(IOException.class)
    public FileChangeMonitor(final File file) {

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
                            notifyListeners(new FileChangeEvent(new File(parentDir.toString(), evt.context().toString())));
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

    public void addFileChangeListener(FileChangeListener fileChangeListener) {
        synchronized (fileChangeListeners) {
            if (!fileChangeListeners.contains(fileChangeListener)) {
                fileChangeListeners.add(fileChangeListener);
            }
        }
    }

    private void notifyListeners(FileChangeEvent fileChangeEvent) {
        FileChangeListener[] listeners;
        synchronized (fileChangeListeners) {
            listeners = new FileChangeListener[fileChangeListeners.size()];
            fileChangeListeners.toArray(listeners);
        }

        for (FileChangeListener listener : listeners) {
            listener.fileChanged(fileChangeEvent);
        }
    }
}
