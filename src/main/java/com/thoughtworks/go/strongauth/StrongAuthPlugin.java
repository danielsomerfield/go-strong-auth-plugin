package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.handlers.Handlers;
import com.thoughtworks.go.strongauth.util.Action;
import com.thoughtworks.go.strongauth.util.Wrapper;

import static com.thoughtworks.go.strongauth.util.Logging.withLogging;

@Extension
public class StrongAuthPlugin implements GoPlugin {

    private Handlers handlers = ComponentFactory.handlers();
    private final GoPluginIdentifier goPluginIdentifier;
    private Wrapper<GoApplicationAccessor> accessorWrapper = new Wrapper<>();

    public StrongAuthPlugin() {
        goPluginIdentifier = ComponentFactory.goPluginIdentifier();
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
                return handlers.get(request.requestName()).call(request);
            }
        });
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