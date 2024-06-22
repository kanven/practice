package com.kanven.practice.file.extension;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;


@Slf4j
public final class ServiceLoader<T> {

    private static final String PREFIX = "META-INF/services/";

    private Map<String, Class<T>> classes = new HashMap<>(0);

    private Class<T> clazz;

    public ServiceLoader(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClass(String name) {
        return classes.get(name);
    }

    public void loadExtension(Class<T> type, ClassLoader loader) throws IOException {
        Set<String> names = loadMetaInformation(type, loader);
        List<Class<T>> classes = loadClass(names, loader);
        classes.forEach(clazz -> {
            String name = getSpiMeta(clazz);
            if (StringUtils.isBlank(name)) {
                //TODO throw an exception
            } else {
                if (this.classes.containsKey(name)) {
                    //TODO throw an exception
                } else {
                    //check
                    checkClass(clazz);
                    this.classes.put(name, clazz);
                }
            }
        });
    }

    private void checkClass(Class<T> clazz) {

    }

    public static <T> Scope getScope(Class<T> clazz) {
        Spi spi = clazz.getAnnotation(Spi.class);
        return spi.scope();
    }

    public static <T> String getSpiMeta(Class<T> clazz) {
        SpiMate meta = clazz.getAnnotation(SpiMate.class);
        if (meta == null) {
            return null;
        }
        return meta.name();
    }

    private List<Class<T>> loadClass(Set<String> names, ClassLoader loader) {
        List<Class<T>> classes = new ArrayList<>(0);
        names.forEach(name -> {
            Class<T> clazz;
            try {
                if (loader == null) {
                    clazz = (Class<T>) Class.forName(name);
                } else {
                    clazz = (Class<T>) Class.forName(name, true, loader);
                }
                classes.add(clazz);
            } catch (Exception e) {

            }
        });
        return classes;
    }


    private Set<String> loadMetaInformation(Class<T> type, ClassLoader loader) throws IOException {
        String path = PREFIX + type.getName();
        Enumeration<URL> urls;
        if (loader == null) {
            urls = ClassLoader.getSystemResources(path);
        } else {
            urls = loader.getResources(path);
        }
        Set<String> names = new HashSet<>(0);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            try (InputStream in = url.openStream()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.defaultCharset()))) {
                    String line;
                    while (StringUtils.isNoneBlank(line = reader.readLine())) {
                        line = StringUtils.trim(line);
                        parseLine(line, names);
                    }
                }
            }
        }
        return names;
    }

    private void parseLine(String line, Set<String> names) {
        int pos = line.indexOf('#');
        if (pos > 0) {
            line = line.substring(0, pos);
        }
        line = StringUtils.trim(line);
        if (StringUtils.isBlank(line)) {
            return;
        }
        if ((line.indexOf(' ') >= 0) || (line.indexOf('\t') >= 0)) {
            return;
        }
        int cp = line.codePointAt(0);
        if (!Character.isJavaIdentifierStart(cp)) {
            return;
        }
        for (int i = Character.charCount(cp); i < line.length(); i += Character.charCount(cp)) {
            cp = line.codePointAt(i);
            if (!Character.isJavaIdentifierPart(cp) && (cp != '.')) {
                return;
            }
        }
        names.add(line);
    }

}
