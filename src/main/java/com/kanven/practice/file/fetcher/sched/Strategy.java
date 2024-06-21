package com.kanven.practice.file.fetcher.sched;

public interface Strategy<E> {

    void add(E entry);

    E pickOut();

}
