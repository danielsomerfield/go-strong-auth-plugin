package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Extension
public class StrongAuthPlugin implements GoPlugin {
    public static final String EXTENSION_NAME = "authentication";
    public static final String PLUGIN_CONFIGURATION = "go.authentication.plugin-configuration";
    public static final int SUCCESS_RESPONSE_CODE = 200;
    public static final int REDIRECT_RESPONSE_CODE = 302;
    public static final int INTERNAL_ERROR_RESPONSE_CODE = 500;
    private static final List<String> goSupportedVersions = asList("1.0");
    private static Logger LOGGER = Logger.getLoggerFor(StrongAuthPlugin.class);
    private GoApplicationAccessor goApplicationAccessor;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        this.goApplicationAccessor = goApplicationAccessor;
    }

    @Override
    @SneakyThrows
    public GoPluginApiResponse handle(GoPluginApiRequest goPluginApiRequest) {
        LOGGER.info(String.format("Received message: %s -- %s -- %s", goPluginApiRequest.requestParameters(), goPluginApiRequest.requestName(), goPluginApiRequest.requestBody()));
        String requestName = goPluginApiRequest.requestName();
        if (requestName.equals(PLUGIN_CONFIGURATION)) {
            return handlePluginConfigurationRequest();
        } else if (requestName.equals("go.plugin-settings.get-configuration")) {
            return handlePluginConfigurationRequest();
        } else if (requestName.equals("go.plugin-settings.get-view")) {
            Map<String, Object> response = new HashMap<>();
            response.put("template", IOUtils.toString(getClass().getResourceAsStream("/plugin-settings.template.html"), "UTF-8"));
            return renderResponse(SUCCESS_RESPONSE_CODE, null, JSONUtils.toJSON(response));
        } else {
            return renderResponse(404, null, null);
        }
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return getGoPluginIdentifier();
    }

    private GoPluginApiResponse handlePluginConfigurationRequest() {
        Map<String, Object> response = new HashMap<>();
        response.put("foo", createField("Server Base URL", null, true, false, "0"));
        return renderResponse(SUCCESS_RESPONSE_CODE, null, JSONUtils.toJSON(response));
    }

    private Map<String, Object> createField(String displayName, String defaultValue, boolean isRequired, boolean isSecure, String displayOrder) {
        Map<String, Object> fieldProperties = new HashMap<String, Object>();
        fieldProperties.put("display-name", displayName);
        fieldProperties.put("default-value", defaultValue);
        fieldProperties.put("required", isRequired);
        fieldProperties.put("secure", isSecure);
        fieldProperties.put("display-order", displayOrder);
        return fieldProperties;
    }

    private GoPluginIdentifier getGoPluginIdentifier() {
        return new GoPluginIdentifier(EXTENSION_NAME, goSupportedVersions);
    }

    private GoPluginApiResponse renderResponse(final int responseCode, final Map<String, String> responseHeaders, final String responseBody) {
        return new GoPluginApiResponse() {
            @Override
            public int responseCode() {
                return responseCode;
            }

            @Override
            public Map<String, String> responseHeaders() {
                return responseHeaders;
            }

            @Override
            public String responseBody() {
                return responseBody;
            }
        };
    }
}