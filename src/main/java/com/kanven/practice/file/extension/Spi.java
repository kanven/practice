package com.kanven.practice.file.extension;

public @interface Spi {

    Scope scope() default Scope.SINGLETON;

}
