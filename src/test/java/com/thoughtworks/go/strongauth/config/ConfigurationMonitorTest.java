package com.thoughtworks.go.strongauth.config;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class ConfigurationMonitorTest {

    @Test
    public void testChangedConfigFiresEvent() {
        final GoAPI goAPI = mock(GoAPI.class);
        PluginConfiguration config1 = new PluginConfiguration("/initial/value");
        PluginConfiguration config2 = new PluginConfiguration("/changed/value");
        when(goAPI.getPluginConfiguration()).thenReturn(config1, config2);
        final List<ConfigurationChangedEvent> events = new ArrayList<>();
        final ConfigurationMonitor configurationMonitor = new ConfigurationMonitor(goAPI, ImmutableList.of(
                new ConfigurationChangeListener() {
                    @Override
                    public void configurationChanged(ConfigurationChangedEvent event) {
                        events.add(event);
                    }

                }
        ), MoreExecutors.directExecutor());

        configurationMonitor.check();
        assertThat(events.size(), is(0));
        configurationMonitor.check();
        assertThat(events.size(), is(0));
        assertThat(events.get(0), is(new ConfigurationChangedEvent(new PluginConfiguration("/changed/value"))));
    }
}