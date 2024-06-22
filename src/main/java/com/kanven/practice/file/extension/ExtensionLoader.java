package com.kanven.practice.file.extension;

import java.util.List;

public interface ExtensionLoader<T> {

    T getExtension(String name);

    T getExtension(String name, List<Object> params);

}
