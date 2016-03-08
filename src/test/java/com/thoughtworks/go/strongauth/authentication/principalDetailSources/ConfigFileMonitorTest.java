package com.thoughtworks.go.strongauth.authentication.principalDetailSources;

import com.thoughtworks.go.strongauth.config.ConfigurationMonitor;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@Ignore
public class ConfigFileMonitorTest {

    @Test
    public void testMonitorDifferentFileWhenConfigChanges() {
        ConfigurationMonitor configurationMonitor = mock(ConfigurationMonitor.class);
//        ConfigFileMonitor configFileMonitor = new ConfigFileMonitor(
//            configurationMonitor,
//
//        );
    }
}