package com.kanven.practice.file.sched.strategy;

import com.kanven.practice.file.extension.Scope;
import com.kanven.practice.file.extension.Spi;

@Spi(scope = Scope.SINGLETON)
public interface Strategy<E> {

    void add(E entry);

    E pickOut();

}
