package com.thoughtworks.go.strongauth.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.thoughtworks.go.strongauth.util.io.SourceChangeEvent;

import java.util.List;
import java.util.concurrent.Executor;

public class ConfigurationMonitor {

    private final GoAPI goAPI;
    private final List<? extends ConfigurationChangeListener> configurationChangeListeners;
    private final Executor executorService;
    private boolean running = true;

    private static final int MIN_DELAY = 100;
    private static final int MAX_DELAY = 2000;
    private Function<Integer, Integer> incrementalChange = new Function<Integer, Integer>() {
        @Override
        public Integer apply(final Integer in) {
            if (in > MAX_DELAY) {
                return MAX_DELAY;
            }
            return in * 2;
        }
    };

    public ConfigurationMonitor(
            final GoAPI goAPI,
            final List<? extends ConfigurationChangeListener> configurationChangeListeners,
            final Executor executorService
    ) {
        this.goAPI = goAPI;
        this.configurationChangeListeners = configurationChangeListeners;
        this.executorService = executorService;
    }

    void check() {
//        final int newDelay;
//
//        final PluginConfiguration newConfiguration = goAPI.getPluginConfiguration();
//        if (!newConfiguration.equals(currentConfiguration)) {
//            notifyListeners(newConfiguration);
//            newDelay = MIN_DELAY;
//        }
//
//        final int newDelay;
//        if (!newMaybeHash.equals(maybeHash)) {
//            notifyListeners(new SourceChangeEvent(contents(), file.getAbsolutePath()));
//
//        } else {
//            newDelay = incrementalChange.apply(delay);
//        }
//
//        if (running) {
//            waitFor(newDelay);
//            check(newConfiguration);
//        }
    }

    private void waitFor(final int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
        }
    }

    private void notifyListeners(final PluginConfiguration pluginConfiguration) {
        final ConfigurationChangeListener[] listeners;
        synchronized (configurationChangeListeners) {
            listeners = new ConfigurationChangeListener[configurationChangeListeners.size()];
            configurationChangeListeners.toArray(listeners);
        }

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final ConfigurationChangedEvent event = new ConfigurationChangedEvent(pluginConfiguration);
                for (ConfigurationChangeListener listener : listeners) {
                    listener.configurationChanged(event);
                }
            }
        });
    }
}
