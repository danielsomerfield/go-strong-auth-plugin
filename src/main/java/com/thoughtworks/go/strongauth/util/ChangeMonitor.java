package com.thoughtworks.go.strongauth.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChangeMonitor<EVT_TYPE> {

    private static final long MIN_DELAY = 100;
    private static final long MAX_DELAY = 2000;

    private boolean running = true;
    private FileChangeDelegate<EVT_TYPE> delegate;
    private final List<MonitorListener> changeListeners = new LinkedList<>();
    private final static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);


    public ChangeMonitor(FileChangeDelegate<EVT_TYPE> delegate) {
        this.delegate = delegate;
        checkForChange(MIN_DELAY, delegate.newValue());
    }

    private Function<Long, Long> incrementalChange = new Function<Long, Long>() {
        @Override
        public Long apply(final Long in) {
            if (in > MAX_DELAY) {
                return MAX_DELAY;
            }
            return in * 2;
        }
    };

    private void checkForChange(final long delay, final Optional<?> oldMaybeValue) {
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                final Optional<?> newMaybeValue = delegate.newValue();
                final long newDelay;
                if (delegate.valueChanged(newMaybeValue, oldMaybeValue)) {
                    final EVT_TYPE notifyEvent = delegate.createNotifyEvent(newMaybeValue, oldMaybeValue);
                    notifyListeners(notifyEvent);
                    newDelay = MIN_DELAY;
                } else {
                    newDelay = incrementalChange.apply(delay);
                }

                if (running) {
                    checkForChange(newDelay, newMaybeValue);
                }
            }

        }, delay, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        running = false;
    }

    private void notifyListeners(final EVT_TYPE sourceChangeEvent) {
        final MonitorListener[] listeners;
        synchronized (changeListeners) {
            listeners = new MonitorListener[changeListeners.size()];
            changeListeners.toArray(listeners);
        }

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for (MonitorListener<EVT_TYPE> listener : listeners) {
                    listener.changed(sourceChangeEvent);
                }
            }
        });
    }

    public void addChangeListener(MonitorListener<EVT_TYPE> sourceChangeListener) {
        synchronized (changeListeners) {
            if (!changeListeners.contains(sourceChangeListener)) {
                changeListeners.add(sourceChangeListener);
            }
        }
    }

    public interface FileChangeDelegate<EVENT_TYPE> {
        EVENT_TYPE createNotifyEvent(Optional<?> newMaybeValue, Optional<?> oldMaybeValue);

        boolean valueChanged(Optional<?> newMaybeHash, Optional<?> maybeHash);

        Optional<?> newValue();
    }

    public interface MonitorListener<T> {
        void changed(T event);
    }
}
