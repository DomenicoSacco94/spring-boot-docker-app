package com.de.hiking.utils;

import org.slf4j.Logger;

import java.util.Arrays;

/**
 * Class containing utility <code>Logger</code> methods to track function calls and log <code>Exceptions</code>
 */
public class LoggerUtil {

    /**
     * Logs enter method state.
     *
     * @param logger  the logger
     * @param objects the objects
     */
    public static void logEnterMethod(Logger logger, Object... objects) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.debug("Entering method: {}, with args {}", methodName, Arrays.toString(objects));
    }

    /**
     * Logs exit method state.
     *
     * @param logger the logger
     * @param result the result
     */
    public static void logExitMethod(Logger logger, Object result) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.debug("Entering method: {}, with result {}", methodName, result);
    }

    /**
     * Logs an exception
     *
     * @param logger    the logger
     * @param exception the result
     */
    public static void logException(Logger logger, Exception exception) {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.warn("Exception in method : {}, with message {}", methodName, exception.getMessage());
    }
}
