package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.handlers.Handlers;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.go.strongauth.wire.GoUserEncoder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

@Extension
public class StrongAuthPlugin implements GoPlugin {

    public final String pluginId = Constants.PLUGIN_ID;
    private static final Logger LOGGER = Logger.getLoggerFor(StrongAuthPlugin.class);

    private ComponentFactory componentFactory;
    private GoPluginIdentifier goPluginIdentifier = new GoPluginIdentifier("authentication", asList("1.0"));
    private Handlers handlers;

    public StrongAuthPlugin() {
        try {
            componentFactory = ComponentFactory.create();
            this.handlers = componentFactory.handlers();
        } catch (Exception e) {
            LOGGER.error("Failed to create component factory", e);
            throw e;
        }
    }

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {

    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return goPluginIdentifier;
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        return handlers.get(request.requestName()).call(request);
    }

}