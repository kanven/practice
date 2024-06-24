package com.kanven.practice.file.sched.executor;

import com.kanven.practice.file.extension.Scope;
import com.kanven.practice.file.extension.Spi;

@Spi(scope = Scope.PROTOTYPE)
public interface Filter {

    boolean filter(String line);

}
