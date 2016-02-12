package com.thoughtworks.go.strongauth.config;

import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompositeAuthConfigurationTest {

    private StrongAuthPluginConfigurationProvider firstConfiguration;
    private StrongAuthPluginConfigurationProvider secondConfiguration;
    private StrongAuthPluginConfiguration configuration;

    @Before
    public void setup() {
        firstConfiguration = mock(StrongAuthPluginConfigurationProvider.class);
        secondConfiguration = mock(StrongAuthPluginConfigurationProvider.class);
        configuration = new StrongAuthPluginConfiguration(
                firstConfiguration, secondConfiguration
        );
    }

    @Test
    public void testGetPasswordFileLocationWhenFirstProviderHasValue() {

        when(firstConfiguration.getPasswordFileLocation()).thenReturn(Optional.of("firstLocation"));
        when(secondConfiguration.getPasswordFileLocation()).thenReturn(Optional.of("secondLocation"));
        Optional<File> file = configuration.getPasswordFileLocation();

        assertThat(file, is(Optional.of(new File("firstLocation"))));
    }

    @Test
    public void testGetPasswordFileLocationWhenFirstProviderHasNoValue() {

        when(firstConfiguration.getPasswordFileLocation()).thenReturn(Optional.<String>absent());
        when(secondConfiguration.getPasswordFileLocation()).thenReturn(Optional.of("secondLocation"));

        Optional<File> file = configuration.getPasswordFileLocation();

        assertThat(file, is(Optional.of(new File("secondLocation"))));
    }

    @Test
    public void testGetPasswordReturnsAbsentWhenProvidersAreAbsent() {

        when(firstConfiguration.getPasswordFileLocation()).thenReturn(Optional.<String>absent());
        when(secondConfiguration.getPasswordFileLocation()).thenReturn(Optional.<String>absent());

        Optional<File> file = configuration.getPasswordFileLocation();

        assertThat(file, is(Optional.<File>absent()));
    }
}
