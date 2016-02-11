package com.thoughtworks.go.strongauth.handlers;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.util.Action;
import com.thoughtworks.go.strongauth.util.MapBuilder;
import com.thoughtworks.go.strongauth.util.Resources;
import com.thoughtworks.go.strongauth.util.Wrapper;
import static com.thoughtworks.go.strongauth.util.Constants.*;

import java.util.Map;

import static com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse.SUCCESS_RESPONSE_CODE;
import static com.thoughtworks.go.strongauth.util.Json.toJson;
import static com.thoughtworks.go.strongauth.util.Json.toMapOfStrings;
import static com.thoughtworks.go.strongauth.util.Logging.withLogging;
import static com.thoughtworks.go.strongauth.util.MapBuilder.create;
import static com.thoughtworks.go.strongauth.util.Resources.get;

/* The first call that will be provided to the plugin. This happens on clicking of the image.
 * The plugin can then return a 200 response with some HTML (for a page it controls) or a 302 response (with an
 * appropriate "Location" header and redirect to somewhere else. At some point, it needs to call
 * the accessor and authenticate the user and then redirect to the base URL of the server. */
public class PluginIndexRequestHandler implements Handler {
    private final Logger logger = Logger.getLoggerFor(PluginIndexRequestHandler.class);

    private Wrapper<GoApplicationAccessor> accessorWrapper;
    private GoPluginIdentifier goPluginIdentifier;

    public PluginIndexRequestHandler(Wrapper<GoApplicationAccessor> accessorWrapper, GoPluginIdentifier goPluginIdentifier) {
        this.accessorWrapper = accessorWrapper;
        this.goPluginIdentifier = goPluginIdentifier;
    }

    @Override
    public GoPluginApiResponse call(GoPluginApiRequest request) {
        Map<String, String> settings = fetchPluginSettings();

        if (pluginHasNotBeenSetup(settings)) {
            return responseAboutPluginSettingsNotBeingProvided();
        }
        return responseAfterAuthentication(settings);
    }

    private boolean pluginHasNotBeenSetup(Map<String, String> settings) {
        return settings == null ||
                (!settings.containsKey(SETTINGS_USERNAME_KEY) || "".equals(settings.get(SETTINGS_USERNAME_KEY))) ||
                (!settings.containsKey(SETTINGS_USER_DISPLAY_NAME_KEY) || "".equals(settings.get(SETTINGS_USER_DISPLAY_NAME_KEY)));
    }

    private GoPluginApiResponse responseAboutPluginSettingsNotBeingProvided() {
        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE);
        response.setResponseBody(get("/plugin-has-not-been-setup.html"));
        return response;
    }

    private GoPluginApiResponse responseAfterAuthentication(Map<String, String> settings) {
        authenticateGuestUser(settings);

        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(302);
        response.addResponseHeader("Location", "/");
        return response;
    }

    /* Make a call to the GoCD server and fetch the configured settings for this plugin. */
    private Map<String, String> fetchPluginSettings() {
        Map<String, String> requestDetails = MapBuilder.<String, String>create().add("plugin-id", PLUGIN_ID).build();

        DefaultGoApiRequest apiRequest = new DefaultGoApiRequest(API_CALL_PLUGIN_SETTINGS_GET, "1.0", goPluginIdentifier);
        apiRequest.setRequestBody(toJson(requestDetails));
        GoApiResponse apiResponse = makeARequestToTheGoCDServer(apiRequest);

        return toMapOfStrings(apiResponse.responseBody());
    }

    /* Make a call to the GoCD server and mark the guest user as "authenticated". */
    private void authenticateGuestUser(Map<String, String> pluginSettings) {
        String guestUserName = pluginSettings.get(SETTINGS_USERNAME_KEY);

        Map<Object, Object> userObject = create()
                .add(AUTH_API_USER_KEY, create()
                        .add(AUTH_API_USERNAME_KEY, guestUserName)
                        .add(AUTH_API_DISPLAYNAME_KEY, pluginSettings.get(SETTINGS_USER_DISPLAY_NAME_KEY))
                        .add(AUTH_API_EMAIL_KEY, pluginSettings.get(SETTINGS_USER_EMAIL_KEY)).build()
                ).build();

        DefaultGoApiRequest authenticateUserRequest = new DefaultGoApiRequest(API_CALL_AUTHENTICATE_USER, "1.0", goPluginIdentifier);
        authenticateUserRequest.setRequestBody(toJson(userObject));

        GoApiResponse response = makeARequestToTheGoCDServer(authenticateUserRequest);
        if (response.responseCode() != SUCCESS_RESPONSE_CODE) {
            logger.error("Failed to authenticate guest user: " + guestUserName);
        }
    }

    private GoApiResponse makeARequestToTheGoCDServer(DefaultGoApiRequest apiRequest) {
        return withLogging("From plugin to server", apiRequest, new Action<DefaultGoApiRequest, GoApiResponse>() {
            @Override
            public GoApiResponse call(DefaultGoApiRequest request) {
                return accessorWrapper.get().submit(request);
            }
        });
    }
}
