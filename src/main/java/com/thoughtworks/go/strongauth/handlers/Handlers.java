package com.thoughtworks.go.strongauth.handlers;

import java.util.Map;

import static java.lang.String.format;

public class Handlers {

    private final Map<String, Handler> handlers;

    public Handlers(final Map<String, Handler> handlers) {
        this.handlers = handlers;
    }

    public Handler get(String requestName) {
        if (handlers.containsKey(requestName)) {
            return handlers.get(requestName);
        }
        throw new RuntimeException(format("Handler for request of type: %s is not implemented", requestName));
    }
}
