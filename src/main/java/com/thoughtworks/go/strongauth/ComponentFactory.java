package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.strongauth.config.Config;
import com.thoughtworks.go.strongauth.handlers.Handlers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ComponentFactory {

    public static final String CALL_FROM_SERVER_GET_CONFIGURATION = "go.plugin-settings.get-configuration";
    public static final String CALL_FROM_SERVER_AUTHENTICATE_USER = "go.authentication.authenticate-user";
    public static final String CALL_FROM_SERVER_PLUGIN_CONFIGURATION = "go.authentication.plugin-configuration";
    public static final String CALL_FROM_SERVER_GET_VIEW = "go.plugin-settings.get-view";
    public static final String CALL_FROM_SERVER_VALIDATE_CONFIGURATION = "go.plugin-settings.validate-configuration";
    private final Handlers handlers;

    @Autowired
    public ComponentFactory(Handlers handlers) {
        this.handlers = handlers;
    }

    public static ComponentFactory create() {
        try {
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.setClassLoader(ComponentFactory.class.getClassLoader());
            ctx.register(Config.class);
            ctx.refresh();
            return ctx.getBean(ComponentFactory.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Handlers handlers() {
        return handlers;
    }
}
