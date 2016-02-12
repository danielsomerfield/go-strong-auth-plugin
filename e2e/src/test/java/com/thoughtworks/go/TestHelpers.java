package com.thoughtworks.go;


import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;

public class TestHelpers {

    @SneakyThrows
    public void enableAuth() {
        executeDockerCommand("/tmp/test-helpers/enable-auth.py");
    }

    @SneakyThrows
    public void disableAuth() {
        executeDockerCommand("/tmp/test-helpers/disable-auth.py");
    }

    @SneakyThrows
    public void clearAuthFile() {
        executeDockerCommand("/tmp/test-helpers/clear-password-file.py");
    }

    @SneakyThrows
    private void executeDockerCommand(String path) {
        int response = new ProcessBuilder().command(
                "./bin/execute-docker-command.sh",
                path).inheritIO().start().waitFor();
        if (response != 0) {
            throw new RuntimeException(String.format("Failed to execute %s", path));
        }
    }
}
