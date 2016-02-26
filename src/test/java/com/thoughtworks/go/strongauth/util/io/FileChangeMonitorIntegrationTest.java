package com.thoughtworks.go.strongauth.util.io;

import com.google.common.base.Supplier;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FileChangeMonitorIntegrationTest {

    private File path;

    @Before
    public void setup() throws IOException {
        path = File.createTempFile("FileChangeMonitorTest", "tmp");
        setFileContents("foo");
    }

    private void setFileContents(final String contents) throws IOException {
        try (OutputStream out = new FileOutputStream(path)) {
            IOUtils.write(contents, out, "UTF-8");
        }
    }

    @After
    public void teardown() {
        path.delete();
    }


    @Test
    public void detectFileChange() throws Exception {

        final Wrapper<SourceChangeEvent> eventWrapper = new Wrapper<>();
        FileChangeMonitor fileChangeMonitor = new FileChangeMonitor(path);
        fileChangeMonitor.addChangeListener(new SourceChangerListener(){
            @Override
            public void sourceChanged(final SourceChangeEvent event) {
                eventWrapper.set(event);
            }
        });
        setFileContents("foo2");

        waitUntil(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return eventWrapper.isSet();
            }
        }, 5000);
        String newContents = IOUtils.toString(eventWrapper.get().getInputStream());
        assertThat(newContents, is("foo2"));
    }

    private static void waitUntil(Supplier<Boolean> supplier, long waitTimeInMillis) {
        long expireTime = System.currentTimeMillis() + waitTimeInMillis;
        while (System.currentTimeMillis() < expireTime) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}

            if (supplier.get()) {
                return;
            }
        }
        Assert.fail("Expired waiting for condition.");
    }

    private static class Wrapper<T> {
        private T value;

        private T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }

        public boolean isSet() {
            return value != null;
        }
    }

}