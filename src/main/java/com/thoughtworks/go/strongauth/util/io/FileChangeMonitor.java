package com.thoughtworks.go.strongauth.util.io;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.go.strongauth.util.InputStreamSource;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class FileChangeMonitor implements InputStreamSource<File> {

    public final String pluginId = Constants.PLUGIN_ID;
    private static final Logger LOGGER = Logger.getLoggerFor(FileChangeMonitor.class);

    private boolean running = true;
    private final List<SourceChangeListener> sourceChangeListeners = new LinkedList<>();
    private final static ExecutorService executorService = Executors.newCachedThreadPool();
    private final File file;

    private int minDelay = 100;
    private int maxDelay = 2000;
    private Function<Integer, Integer> incrementalChange = new Function<Integer, Integer>() {
        @Override
        public Integer apply(final Integer in) {
            if (in > maxDelay) {
                return maxDelay;
            }
            return in * 2;
        }
    };

    public FileChangeMonitor(final File file) {
        this.file = file;
        checkForChange(minDelay, hash());
    }

    private void checkForChange(final int delay, final Optional<String> maybeHash) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                final Optional<String> newMaybeHash = hash();
                final int newDelay;
                if (!newMaybeHash.equals(maybeHash)) {
                    notifyListeners(new SourceChangeEvent(contents(), file.getAbsolutePath()));
                    newDelay = minDelay;
                } else {
                    newDelay = incrementalChange.apply(delay);
                }

                if (running) {
                    waitFor(newDelay);
                    checkForChange(newDelay, newMaybeHash);
                }
            }

            private InputStream contents() {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    return new ByteArrayInputStream(new byte[0]);
                }
            }
        }, format("Watcher thread: %s", file.getAbsolutePath()));
    }

    private Optional<String> hash() {
        try {
            return Optional.of(md5Hex(new FileInputStream(file)));
        } catch (IOException e) {
            return Optional.absent();
        }
    }

    private void waitFor(final int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void addChangeListener(SourceChangeListener sourceChangeListener) {
        LOGGER.info("Adding listener " + sourceChangeListener);
        synchronized (sourceChangeListeners) {
            if (!sourceChangeListeners.contains(sourceChangeListener)) {
                sourceChangeListeners.add(sourceChangeListener);
            }
        }
    }

    private void notifyListeners(final SourceChangeEvent sourceChangeEvent) {
        //Make this async
        LOGGER.info("notifyListeners from : " + this + " event = " + sourceChangeEvent);
        LOGGER.info("Listeners " + sourceChangeListeners);
        final SourceChangeListener[] listeners;
        synchronized (sourceChangeListeners) {
            listeners = new SourceChangeListener[sourceChangeListeners.size()];
            sourceChangeListeners.toArray(listeners);
        }

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for (SourceChangeListener listener : listeners) {
                    listener.sourceChanged(sourceChangeEvent);
                }
            }
        });

    }

    @Override
    public InputStream inputStream() throws IOException {
        return new FileInputStream(file);
    }
}
