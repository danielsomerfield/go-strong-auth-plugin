package com.thoughtworks.go;


import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class TestHelpers {

    @SneakyThrows
    public void enableAuth() {
        executeHelper("enable-auth.py");
    }

    @SneakyThrows
    public void disableAuth() {
        executeHelper("disable-auth.py");
    }

    @SneakyThrows
    public void clearAuthFile() {
        executeHelper("clear-password-file.py");
    }

    @SneakyThrows
    public void executeHelper(String helperName, String... params) {
        List<String> command = Lists.newArrayList(format("/tmp/test-helpers/%s", helperName));
        command.addAll(asList(params));
        executeDockerCommand(command);
    }

    @SneakyThrows
    private void executeDockerCommand(List<String> commands) {
        List<String> commandList = Lists.newArrayList("./bin/execute-docker-command.sh");
        commandList.addAll(commands);
        int response = new ProcessBuilder().command(commandList).inheritIO().start().waitFor();
        if (response != 0) {
            throw new RuntimeException(format("Failed to execute %s", commandList.toString()));
        }
    }

    public void createPasswordEntryFor(String username, String password) {
        int iterations = 10000;
        int keyLength = 256;
        byte[] salt = createSalt();
        String saltString = Base64.encodeBase64String(salt);
        String hash = createHash(salt, password, iterations, keyLength);
        String entry = format("%s:%s:%s:%s(%s, %s)", username, hash, saltString, "PBKDF2WithHmacSHA1", String.valueOf(iterations), String.valueOf(keyLength));
        executeHelper("insert-password-entry.py", entry);
    }

    @SneakyThrows
    private String createHash(byte[] salt, String password, int iterations, int keyLength) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        byte[] key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).getEncoded();
        return Hex.encodeHexString(key);
    }

    private byte[] createSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
}
