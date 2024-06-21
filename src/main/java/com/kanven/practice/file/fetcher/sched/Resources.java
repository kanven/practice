package com.kanven.practice.file.fetcher.sched;

import com.kanven.practice.file.fetcher.sched.Strategy;

public abstract class Resources<E> {

    private Strategy<E> strategy;

    public abstract void add(E entry);

    public abstract E pickUp();

}
