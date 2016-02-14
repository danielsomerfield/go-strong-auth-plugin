package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.goAPI.GoApplicationAccessorSource;
import com.thoughtworks.go.strongauth.handlers.Handlers;
import com.thoughtworks.go.strongauth.util.Action;

import static com.thoughtworks.go.strongauth.util.Logging.withLogging;

@Extension
public class StrongAuthPlugin implements GoPlugin, GoApplicationAccessorSource {

    private ComponentFactory componentFactory = new ComponentFactory(this);
    private GoPluginIdentifier goPluginIdentifier = componentFactory.goPluginIdentifier();
    private GoApplicationAccessor goApplicationAccessor;
    private Handlers handlers;

    public StrongAuthPlugin() {

    }

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        this.goApplicationAccessor = goApplicationAccessor;
        this.handlers = componentFactory.handlers();
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
                return handlers.get(request.requestName()).call(request);
            }
        });
    }

    @Override
    public GoApplicationAccessor getGoApplicationAccessor() {
        if (this.goApplicationAccessor == null) {
            throw new RuntimeException("You cannot get the accessor until initialization is complete");
        }
        return this.goApplicationAccessor;
    }

//    @Value
//    public static class PluginConfiguration {
//        private final File pluginFilePath;
//    }
//
//    private PluginConfiguration getPluginConfiguration() {
//        return new PluginConfiguration(new File(System.getenv("STRONG_AUTH_FILE_PATH")));
//    }

}