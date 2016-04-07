package com.thoughtworks.go.strongauth.authentication.hash;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BCryptProviderTest {

    public static final String PASSWORD_FILE_LINE = "bcrypt";

    @Test
    public void canHandle() throws Exception {
        assertThat(new BCryptProvider().canHandle(PASSWORD_FILE_LINE), is(true));
    }

}