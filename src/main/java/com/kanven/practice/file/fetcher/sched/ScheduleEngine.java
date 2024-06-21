package com.kanven.practice.file.fetcher.sched;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
public class ScheduleEngine<E> implements Closeable {

    private ExecutorService scheduler;

    private ExecutorService executor;

    private Resources<E> resources;

    private List<Executor<E>> executors;

    public void start() {
        scheduler.submit(() -> {
            while (true) {
                try {
                    E entry = resources.pickUp();
                    executor.submit(() -> {
                        executors.forEach(ec -> {
                            try {
                                ec.execute(entry);
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        });
                    });
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        });
    }

    @Override
    public void close() throws IOException {

    }

}
