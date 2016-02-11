package com.thoughtworks.go.strongauth.util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class Json {
    private static Gson gson = new Gson();

    public static Map<String, String> toMapOfStrings(String jsonValue) {
        Type mapOfStringToStringType = new TypeToken<LinkedTreeMap<String, String>>() {}.getType();
        return gson.fromJson(jsonValue, mapOfStringToStringType);
    }

    public static Map toMap(String jsonValue) {
        return gson.fromJson(jsonValue, Map.class);
    }

    public static String toJson(Object anything) {
        return gson.toJson(anything);
    }
}
