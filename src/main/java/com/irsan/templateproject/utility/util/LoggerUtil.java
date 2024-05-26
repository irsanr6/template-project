package com.irsan.templateproject.utility.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.MissingFormatArgumentException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 24/05/2024
 */
public class LoggerUtil {

    private final Logger LOGGER;

    public <T> LoggerUtil(Class<T> clazz) {
        LOGGER = LoggerFactory.getLogger(clazz);
    }

    public static String generateTraceId() {
        int nextInt = ThreadLocalRandom.current().nextInt(1, 10);
        String nextString = String.format("%02d", nextInt);
        return String.format("%s_%s", System.currentTimeMillis(), nextString);
    }

    public void debug(String logging) {
        LOGGER.debug("\u001B[34m" + logging + "\u001B[0m");
    }

    public void warn(String logging) {
        LOGGER.warn("\u001B[33m" + logging + "\u001B[0m");
    }

    public void info(String logging) {
        LOGGER.info("\u001B[32m" + logging + "\u001B[0m");
    }

    public void error(String logging) {
        LOGGER.error("\u001B[31m" + logging + "\u001B[0m");
    }

    public void debug(String format, Object... args) {
        LOGGER.debug("\u001B[34m" + pretty(format, args) + "\u001B[0m");
    }

    public void warn(String format, Object... args) {
        LOGGER.warn("\u001B[33m" + pretty(format, args) + "\u001B[0m");
    }

    public void info(String format, Object... args) {
        LOGGER.info("\u001B[32m" + pretty(format, args) + "\u001B[0m");
    }

    public void error(String format, Object... args) {
        LOGGER.error("\u001B[31m" + pretty(format, args) + "\u001B[0m");
    }

    private String pretty(String format, Object... args) {
        String reformatMessage = format.replace("{}", "%s");
        try {
            return String.format(reformatMessage, args);
        } catch (MissingFormatArgumentException e) {
            reformatMessage = removeExcessPlaceholders(reformatMessage, args.length);
            return String.format(reformatMessage, args);
        }
    }

    public String removeExcessPlaceholders(String format, int arrayLength) {
        int placeholderCount = format.length() - format.replace("%s", "").length();
        placeholderCount /= 2;

        while (placeholderCount > arrayLength) {
            format = format.replaceFirst(" %s", "");
            placeholderCount--;
        }

        return format;
    }

}
