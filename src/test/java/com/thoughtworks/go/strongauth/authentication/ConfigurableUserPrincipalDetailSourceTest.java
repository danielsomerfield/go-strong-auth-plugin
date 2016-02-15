package com.thoughtworks.go.strongauth.authentication;

import com.google.common.base.Optional;
import com.thoughtworks.go.strongauth.authentication.principalDetailSources.ConfigurableUserPrincipalDetailSource;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ConfigurableUserPrincipalDetailSourceTest {

    @Test
    public void testFindExistingUser() {
        InputStream passwordFile = IOUtils.toInputStream("username1:satlz:hazHash:PBKDF2WithHmacSHA1(5000, 64)");
        PrincipalDetailSource principalDetailSource = new ConfigurableUserPrincipalDetailSource(passwordFile);
        Optional<PrincipalDetail> maybePrincipalDetail = principalDetailSource.byUsername("username1");
        assertThat(maybePrincipalDetail, is(Optional.of(
                new PrincipalDetail("username1", "satlz", "hazHash", "PBKDF2WithHmacSHA1(5000, 64)"))));
    }

    @Test
    public void testFindMissingUser() {
        InputStream passwordFile = IOUtils.toInputStream("username1:satlz:hazHash:PBKDF2WithHmacSHA1(5000, 64)");
        PrincipalDetailSource principalDetailSource = new ConfigurableUserPrincipalDetailSource(passwordFile);
        Optional<PrincipalDetail> maybePrincipalDetail = principalDetailSource.byUsername("missingUser");
        assertThat(maybePrincipalDetail, is(Optional.<PrincipalDetail>absent()));
    }
}