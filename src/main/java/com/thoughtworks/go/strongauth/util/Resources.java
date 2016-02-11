package com.thoughtworks.go.strongauth.util;

import org.apache.commons.io.IOUtils;

public class Resources {
    public static String get(String resourceLocation) {
        try {
            return IOUtils.toString(Resources.class.getResourceAsStream(resourceLocation), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get template from " + resourceLocation);
        }
    }
}
