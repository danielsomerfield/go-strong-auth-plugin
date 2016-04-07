package com.thoughtworks.go.strongauth.authentication.principalDetailSources;

import com.google.common.base.Optional;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetail;
import com.thoughtworks.go.strongauth.authentication.PrincipalDetailSource;
import com.thoughtworks.go.strongauth.util.Constants;
import com.thoughtworks.go.strongauth.util.InputStreamSource;
import com.thoughtworks.go.strongauth.util.io.SourceChangeEvent;
import com.thoughtworks.go.strongauth.util.io.SourceChangeListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.apache.commons.io.IOUtils.toBufferedReader;

public class ConfigurableUserPrincipalDetailSource implements PrincipalDetailSource {
    private final Map<String, PrincipalDetail> principalDetails = new HashMap<>();
    public static final Pattern DETAIL_PATTERN = Pattern.compile("^([^:]+):([^:]+):([^:]*):([^:]+)$");

    private static final Logger LOGGER = Logger.getLoggerFor(ConfigurableUserPrincipalDetailSource.class);
    public final String pluginId = Constants.PLUGIN_ID;

    public ConfigurableUserPrincipalDetailSource(InputStreamSource passwordSource) {
        try {
            loadSource(passwordSource.inputStream());
        } catch (IOException e) {
            LOGGER.warn("Missing password file. No credentials loaded.");
        }

        passwordSource.addChangeListener(new SourceChangeListener() {
            @Override
            public void sourceChanged(SourceChangeEvent event) {
                try {
                    loadSource(event.getInputStream());
                } catch (IOException e) {
                    LOGGER.error("Missing password file. No credentials loaded.");
                }
            }
        });
    }

    private void loadSource(InputStream passwordFile) throws IOException {
        principalDetails.clear();
        String line;
        final BufferedReader bufferedReader = toBufferedReader(new InputStreamReader(passwordFile));
        while ((line = bufferedReader.readLine()) != null) {
            Optional<PrincipalDetail> maybeDetail = parse(line);
            if (maybeDetail.isPresent()) {
                PrincipalDetail detail = maybeDetail.get();
                principalDetails.put(detail.getUsername(), detail);
            }
        }
    }

    private Optional<PrincipalDetail> parse(final String line) {
        final Matcher matcher = DETAIL_PATTERN.matcher(line);
        if (matcher.find()) {
            return Optional.of(new PrincipalDetail(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4))
            );
        } else {
            LOGGER.warn(format("Failed to parse password file line %s", line));
            return Optional.absent();
        }
    }

    @Override
    public Optional<PrincipalDetail> byUsername(String username) {
        return Optional.fromNullable(principalDetails.get(username));
    }

}
