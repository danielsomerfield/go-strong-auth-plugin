package com.thoughtworks.go.strongauth.handlers;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import static com.thoughtworks.go.strongauth.util.Constants.*;
import static com.thoughtworks.go.strongauth.util.Json.toJson;
import static com.thoughtworks.go.strongauth.util.MapBuilder.create;

/* This provides some authentication-plugin-specific configuration information to the GoCD server. Information such
 * as the display image, whether it does password-based auth or web-based auth, etc.
 */
public class PluginConfigurationHandler implements Handler {
    @Override
    public GoPluginApiResponse call(GoPluginApiRequest request) {
        String responseBody = toJson(create()
                .add(CONFIG_DISPLAY_NAME, "Guest login")
//                .add(CONFIG_DISPLAY_IMAGE_URL, getImage())
                .add(CONFIG_WEB_AUTH, true)
                .add(CONFIG_PASSWORD_AUTH, true)
                .build());

        return DefaultGoPluginApiResponse.success(responseBody);
    }

    private String getImage() {
        String imageLocation = "/login_as_guest.png";
        try {
            return "data:image/png;base64," + Base64.encodeBase64String(IOUtils.toByteArray(getClass().getResourceAsStream(imageLocation)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get image from " + imageLocation);
        }
    }
}
