package com.thoughtworks.go.strongauth.handlers;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.util.MapBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.go.strongauth.util.Json.toJson;
import static com.thoughtworks.go.strongauth.util.Json.toMap;
import static com.thoughtworks.go.strongauth.util.MapBuilder.create;
import static com.thoughtworks.go.strongauth.util.Resources.get;


public class PluginSettingsHandler {

    public static final String PLUGIN_CONFIG_PASSWORD_FILE_PATH = "PASSWORD_FILE_PATH";
    public static final String DISPLAY_NAME_PASSWORD_FILE_PATH = "Password file path";

    public static Handler getConfiguration() {
        return new Handler() {
            @Override
            public GoPluginApiResponse call(GoPluginApiRequest request) {
                Map<Object, Object> configResponse = create()
                        .add(PLUGIN_CONFIG_PASSWORD_FILE_PATH, create()
                                .add("display-name", DISPLAY_NAME_PASSWORD_FILE_PATH)
                                .add("required", true))
                        .build();

                return DefaultGoPluginApiResponse.success(toJson(configResponse));
            }
        };
    }

    public static Handler getView() {
        return new Handler() {
            @Override
            public GoPluginApiResponse call(GoPluginApiRequest request) {
                Object viewResponse = create().add("template", get("/plugin-settings.template.html")).build();
                return DefaultGoPluginApiResponse.success(toJson(viewResponse));
            }

        };
    }

    /* Called upon save of the plugin settings in the UI. */
    public static Handler validateConfiguration() {
        return new Handler() {
            @Override
            public GoPluginApiResponse call(GoPluginApiRequest request) {
                final Map<String, Map<String, String>> settings = (Map<String, Map<String, String>>) toMap(request.requestBody()).get("plugin-settings");
                final List<Map<Object, Object>> validationResponse = new ArrayList<>();

                if (isEmpty(settings, PLUGIN_CONFIG_PASSWORD_FILE_PATH)) {
                    addValidationError(validationResponse, PLUGIN_CONFIG_PASSWORD_FILE_PATH, "Password file path");
                }
                return DefaultGoPluginApiResponse.success(toJson(validationResponse));
            }

            private boolean isEmpty(Map<String, Map<String, String>> settings, String key) {
                return !settings.containsKey(key)
                        || settings.get(key) == null
                        || settings.get(key).get("value") == null
                        || "".equals(settings.get(key).get("value").trim());
            }

            private void addValidationError(List<Map<Object, Object>> validationResponse, String key, String userReadableMessagePrefix) {
                String message = userReadableMessagePrefix + " is required and should not be empty";
                validationResponse.add(MapBuilder.create().add("key", key).add("message", message).build());
            }
        };
    }
}
