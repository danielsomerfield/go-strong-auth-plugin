package com.thoughtworks.go.strongauth;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.strongauth.config.Config;
import com.thoughtworks.go.strongauth.handlers.Handlers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ComponentFactory {

    private final Handlers handlers;

    @Autowired
    public ComponentFactory(Handlers handlers) {
        this.handlers = handlers;
    }

    public static ComponentFactory create(GoApplicationAccessor goApplicationAccessor, GoPluginIdentifier goPluginIdentifier) {
        try {
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.setClassLoader(ComponentFactory.class.getClassLoader());
            ctx.register(Config.class);
            ctx.getBeanFactory().registerSingleton("goApplicationAccessor", goApplicationAccessor);
            ctx.getBeanFactory().registerSingleton("goPluginIdentifier", goPluginIdentifier);
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
