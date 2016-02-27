package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Optional;
import com.thoughtworks.go.strongauth.authentication.principalDetailSources.ConfigurableUserPrincipalDetailSource;
import com.thoughtworks.go.strongauth.util.InputStreamSource;
import com.thoughtworks.go.strongauth.util.io.SourceChangeListener;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConfigurableUserPrincipalDetailSourceTest {

    @Test
    public void testFindExistingUser() {
        InputStream passwordFile = IOUtils.toInputStream("username1:satlz:hazHash:PBKDF2WithHmacSHA1(5000, 64)");
        PrincipalDetailSource principalDetailSource = new ConfigurableUserPrincipalDetailSource(new BasicInputStreamSource(passwordFile));
        Optional<PrincipalDetail> maybePrincipalDetail = principalDetailSource.byUsername("username1");
        assertThat(maybePrincipalDetail, is(Optional.of(
                new PrincipalDetail("username1", "satlz", "hazHash", "PBKDF2WithHmacSHA1(5000, 64)"))));
    }

    @Test
    public void testFindMissingUser() {
        InputStream passwordFile = IOUtils.toInputStream("username1:satlz:hazHash:PBKDF2WithHmacSHA1(5000, 64)");
        PrincipalDetailSource principalDetailSource = new ConfigurableUserPrincipalDetailSource(new BasicInputStreamSource(passwordFile));
        Optional<PrincipalDetail> maybePrincipalDetail = principalDetailSource.byUsername("missingUser");
        assertThat(maybePrincipalDetail, is(Optional.<PrincipalDetail>absent()));
    }

    public static class BasicInputStreamSource implements InputStreamSource {

        private InputStream inputStream;

        public BasicInputStreamSource(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void addChangeListener(SourceChangeListener sourceChangeListener) {

        }

        @Override
        public InputStream inputStream() {
            return inputStream;
        }
    }
}