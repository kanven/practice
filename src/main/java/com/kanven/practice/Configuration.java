package com.kanven.practice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class Configuration {

    private static Properties properties;

    private static final String configPath = "leech.properties";

    static {
        Class<Configuration> clazz = Configuration.class;
        ClassLoader loader = clazz.getClassLoader();
        InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(configPath);
        if (input == null) {
            input = loader.getResourceAsStream(configPath);
        }
        if (input == null) {
            input = clazz.getResourceAsStream(configPath);
        }
        Properties properties = new Properties();
        try {
            properties.load(input);
        } catch (Exception e) {
            log.error(configPath + " load has an error!", e);
        }
        Configuration.properties = properties;
    }

    public static String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (StringUtils.isNoneBlank(value)) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    public static long getLong(String key, long defaultValue) {
        String value = properties.getProperty(key);
        if (StringUtils.isNoneBlank(value)) {
            return Long.parseLong(value);
        }
        return defaultValue;
    }

    public static int getInteger(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (StringUtils.isNoneBlank(value)) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    public final static String LEECH_CHARSET = "leech.charset";

    public final static String LEECH_DIR = "leech.dir";

    public final static String LEECH_DIR_WATCHER_RECURSION = "leech.dir.watcher.recursion";

    public final static String LEECH_DIR_WATCHER_NAME = "leech.dir.watcher.name";

    public final static String LEECH_BULK_READER_NAME = "leech.bulk.reader.name";

    public final static String LEECH_BULK_FETCH_HISTORY = "leech.bulk.reader.fetch.history";

    public final static String LEECH_SCHED_STRATEGY_NAME = "leech.sched.strategy.name";

    public final static String LEECH_SCHED_EXECUTOR_THREAD_CORE = "leech.sched.executor.thread.core";

    public final static String LEECH_SCHED_EXECUTOR_THREAD_MAX = "leech.sched.executor.thread.max";

    public final static String LEECH_EXECUTOR_MODE = "leech.executor.mode";

    public final static String LEECH_EXECUTOR_LOG_REGULAR = "leech.executor.log.regular";

    public final static String LEECH_EXECUTOR_LOG_FILTER ="leech.executor.log.filter";

}
