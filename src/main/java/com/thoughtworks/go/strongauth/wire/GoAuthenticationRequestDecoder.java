package com.thoughtworks.go.strongauth.wire;

import com.google.common.base.Optional;
import com.google.gson.*;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.strongauth.authentication.AuthenticationRequest;
import com.thoughtworks.go.strongauth.util.Constants;

import java.io.IOException;
import java.lang.reflect.Type;

import static java.lang.String.format;

public class GoAuthenticationRequestDecoder {

    public final String pluginId = Constants.PLUGIN_ID;
    private static final Logger LOGGER = Logger.getLoggerFor(GoAuthenticationRequestDecoder.class);

    private static Gson gson = new GsonBuilder().registerTypeAdapter(
            AuthenticationRequest.class, new JsonDeserializer<AuthenticationRequest>() {
                @Override
                public AuthenticationRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    if (!json.isJsonObject()) {
                        throw new JsonParseException("Malformed json for AuthenticationRequest");
                    }

                    return new AuthenticationRequest(
                            getAsStringOrFail(json.getAsJsonObject(), "username"),
                            getAsStringOrFail(json.getAsJsonObject(), "password")
                    );

                }
            }).create();

    public static String getAsStringOrFail(JsonObject object, String fieldName) throws JsonParseException {
        JsonElement username = object.get(fieldName);
        if (username != null && username.isJsonPrimitive()) {
            return username.getAsString();
        } else {
            throw new JsonParseException(format("Expected a string value for field %s", fieldName));
        }
    }

    public Optional<AuthenticationRequest> decode(GoPluginApiRequest goRequest) {
        try {
            AuthenticationRequest request = gson.getAdapter(AuthenticationRequest.class).fromJson(goRequest.requestBody());
            return hasRequiredFields(request) ? Optional.of(request) : Optional.<AuthenticationRequest>absent();
        } catch (IOException e) {
            LOGGER.warn("IOException when parsing incoming authentication request.", e);
            return Optional.absent();
        } catch (JsonParseException e) {
            LOGGER.warn("Failure when parsing incoming authentication request.", e);
            return Optional.absent();
        }
    }

    private boolean hasRequiredFields(AuthenticationRequest request) {
        return request.getPassword() != null && request.getUsername() != null;
    }

}
