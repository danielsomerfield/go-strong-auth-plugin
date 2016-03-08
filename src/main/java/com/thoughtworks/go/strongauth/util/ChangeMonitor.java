package com.thoughtworks.go.strongauth.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.logging.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ChangeMonitor<EVT_TYPE, VALUE_TYPE> {

    private static final long MIN_DELAY = 100;
    private static final long MAX_DELAY = 2000;

    private boolean running = true;
    private ChangeMonitorDelegate<? extends EVT_TYPE, VALUE_TYPE> delegate;
    private final List<MonitorListener<EVT_TYPE>> changeListeners = new LinkedList<>();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    private static final Logger LOGGER = Logger.getLoggerFor(ChangeMonitor.class);
    public final String pluginId = Constants.PLUGIN_ID;

    public ChangeMonitor(ChangeMonitorDelegate<? extends EVT_TYPE, VALUE_TYPE> delegate) {
        this.delegate = delegate;
    }

    public void start() {
        checkForChange(MIN_DELAY, Optional.<VALUE_TYPE>absent());
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

    private void checkForChange(final long delay, final Optional<VALUE_TYPE> oldMaybeValue) {
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    final Optional<VALUE_TYPE> newMaybeValue = delegate.newValue();
                    final long newDelay;
                    if (valueChanged(newMaybeValue, oldMaybeValue)) {
                        LOGGER.info(String.format("Value changed from %s to %s", oldMaybeValue, newMaybeValue));
                        LOGGER.info(String.format("Instance: %s", this));
                        final EVT_TYPE notifyEvent = delegate.createNotifyEvent(newMaybeValue, oldMaybeValue);
                        notifyListeners(notifyEvent);
                        newDelay = MIN_DELAY;
                    } else {
                        newDelay = incrementalChange.apply(delay);
                    }

                    if (running) {
                        checkForChange(newDelay, newMaybeValue);
                    }
                } catch (Exception e) {
                    LOGGER.error("ChangeMonitor failed.", e);
                }
            }

        }, delay, TimeUnit.MILLISECONDS);

    }

    private boolean valueChanged(Optional<?> newValue, Optional<?> oldValue) {
        return !oldValue.equals(newValue);
    }

    public void stop() {
        synchronized (changeListeners) {
            changeListeners.clear();
        }
        running = false;
    }

    private void notifyListeners(final EVT_TYPE event) {
        final List<MonitorListener<EVT_TYPE>> listeners;
        synchronized (changeListeners) {
            listeners = new ArrayList<>(changeListeners);
        }

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                for (MonitorListener<EVT_TYPE> listener : listeners) {
                    listener.changed(event);
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

    public interface ChangeMonitorDelegate<EVENT_TYPE, VALUE_TYPE> {
        EVENT_TYPE createNotifyEvent(Optional<VALUE_TYPE> newMaybeValue, Optional<VALUE_TYPE> oldMaybeValue);

        Optional<VALUE_TYPE> newValue();
    }

    public interface MonitorListener<T> {
        void changed(T event);
    }
}
