package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import com.thoughtworks.go.strongauth.handlers.Handlers;

@Extension
public class StrongAuthPlugin implements GoPlugin {

    private ComponentFactory componentFactory = new ComponentFactory();
    private GoPluginIdentifier goPluginIdentifier = componentFactory.goPluginIdentifier();
    private Handlers handlers;


    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
        this.handlers = componentFactory.handlers();
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return goPluginIdentifier;
    }

    /* This is where all calls from the server come to, and have to be handled. */
    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        return handlers.get(request.requestName()).call(request);
    }

}