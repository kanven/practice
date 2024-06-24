package com.kanven.practice.file.sched.executor;

import com.kanven.practice.file.extension.Scope;
import com.kanven.practice.file.extension.Spi;

@Spi(scope = Scope.SINGLETON)
public interface Executor<E> {

    void execute(E entry);

}
