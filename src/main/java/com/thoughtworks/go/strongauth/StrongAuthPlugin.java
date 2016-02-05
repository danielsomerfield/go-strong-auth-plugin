package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.AbstractGoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.List;

import static java.util.Collections.singletonList;

@Extension
public class StrongAuthPlugin extends AbstractGoPlugin {

    private static final List<String> SUPPORTED_VERSIONS = singletonList("1.0");
    private static Logger LOGGER = Logger.getLoggerFor(StrongAuthPlugin.class);

    public static final String PLUGIN_ID = "thoughtworks.strongauth";
    public static final String EXTENSION_NAME = "strongauth";

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest requestMessage) throws UnhandledRequestTypeException {
        throw new UnsupportedOperationException();
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier(EXTENSION_NAME, SUPPORTED_VERSIONS);
    }
}