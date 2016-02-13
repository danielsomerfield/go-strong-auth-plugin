package com.thoughtworks.go.strongauth.goAPI;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class GoUserAPITest {

    @Test
    @Ignore
    public void testAuthenticateUser() throws Exception {
        GoUser goUser = new GoUser("go-uzer");
        GoUserAPI goUserAPI = new GoUserAPI();
        goUserAPI.authenticateUser(goUser);
    }
}