package com.thoughtworks.go.strongauth.util;

import com.thoughtworks.go.plugin.api.logging.Logger;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Logging {
    private static final Logger logger = Logger.getLoggerFor(Logging.class);

    public static <RequestType, ReturnType> ReturnType withLogging(String messagePrefix, RequestType request, Action<RequestType, ReturnType> action) {
        String stringRepresentationOfRequest = ToStringBuilder.reflectionToString(request);

        logger.info(messagePrefix + " - Got: " + stringRepresentationOfRequest);
        try {

            ReturnType response = action.call(request);
            logger.info(messagePrefix + " - Response: " + ToStringBuilder.reflectionToString(response));
            return response;

        } catch (RuntimeException e) {
            String failureMessage = messagePrefix + " -     Failed to handle request";
            logger.error(failureMessage + ": " + stringRepresentationOfRequest);
            throw e;
        }
    }


}
