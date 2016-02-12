package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.handlers.*;
import com.thoughtworks.go.strongauth.util.Action;
import com.thoughtworks.go.strongauth.util.Wrapper;
import lombok.Value;

import static com.thoughtworks.go.strongauth.util.Constants.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.thoughtworks.go.strongauth.util.Logging.withLogging;
import static java.util.Arrays.asList;

@Extension
public class StrongAuthPlugin implements GoPlugin {
    private final Map<String, Handler> handlers;
    private final GoPluginIdentifier goPluginIdentifier;
    private Wrapper<GoApplicationAccessor> accessorWrapper = new Wrapper<GoApplicationAccessor>();

    public StrongAuthPlugin() {
        goPluginIdentifier = new GoPluginIdentifier("authentication", asList("1.0"));

        handlers = new HashMap<>();

        handlers.put(CALL_FROM_SERVER_GET_CONFIGURATION, PluginSettingsHandler.getConfiguration());
        handlers.put(CALL_FROM_SERVER_GET_VIEW, PluginSettingsHandler.getView());
        handlers.put(CALL_FROM_SERVER_VALIDATE_CONFIGURATION, PluginSettingsHandler.validateConfiguration());

        handlers.put(CALL_FROM_SERVER_PLUGIN_CONFIGURATION, new PluginConfigurationHandler());
//        handlers.put(CALL_FROM_SERVER_AUTHENTICATE_USER, new AuthenticationHandler());
        handlers.put(CALL_FROM_SERVER_SEARCH_USER, new SearchUserHandler());
        handlers.put(CALL_FROM_SERVER_INDEX, new PluginIndexRequestHandler(accessorWrapper, goPluginIdentifier));
    }

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        /* This call happens too late for the handlers to be given the accessor. So, give them a
         * wrapper instead (earlier, in constructor) and fill up the wrapper with the accessor now. */
        accessorWrapper.holdOnTo(goApplicationAccessor);
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return goPluginIdentifier;
    }

    /* This is where all calls from the server come to, and have to be handled. */
    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        return withLogging("From server to plugin", request, new Action<GoPluginApiRequest, GoPluginApiResponse>() {
            public GoPluginApiResponse call(GoPluginApiRequest request) {
                if (handlers.containsKey(request.requestName())) {
                    return handlers.get(request.requestName()).call(request);
                }
                throw new RuntimeException("Handler for request of type: " + request.requestName() + " is not implemented");
            }
        });
    }

    @Value
    public static class PluginConfiguration {
        private final File pluginFilePath;
    }

    private PluginConfiguration getPluginConfiguration() {
        return new PluginConfiguration(new File(System.getenv("STRONG_AUTH_FILE_PATH")));
    }
}