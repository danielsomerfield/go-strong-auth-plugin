package com.thoughtworks.go.strongauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JSONUtils {
//    public static Object fromJSON(String json) {
//        return new GsonBuilder().create().fromJson(json, Object.class);
//    }

    @SneakyThrows //TODO: Option
    public static String toJSON(Object object) {
        return new ObjectMapper().writeValueAsString(object);
    }
}
