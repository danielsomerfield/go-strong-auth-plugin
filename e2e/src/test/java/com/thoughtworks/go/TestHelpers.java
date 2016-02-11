package com.thoughtworks.go;


import lombok.SneakyThrows;

public class TestHelpers {

    private static final TestHelpers GO_CONFIGURATION = new TestHelpers();

    public static TestHelpers get() {
        return GO_CONFIGURATION;
    }

    @SneakyThrows
    public void enableAuth() {
        int response = new ProcessBuilder().command(
                "./bin/execute-docker-command.sh",
                "/tmp/test-helpers/enable-auth.py").inheritIO().start().waitFor();
        if (response != 0) {
            throw new RuntimeException("Failed to enable auth");
        }
    }

    @SneakyThrows
    public void disableAuth() {
        int response = new ProcessBuilder().command(
                "./bin/execute-docker-command.sh",
                "/tmp/test-helpers/disable-auth.py").inheritIO().start().waitFor();
        if (response != 0) {
            throw new RuntimeException("Failed to enable auth");
        }
    }
}
