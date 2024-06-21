package com.kanven.practice.file.fetcher.sched;

public interface Executor<E> {

    void execute(E entry);

}
