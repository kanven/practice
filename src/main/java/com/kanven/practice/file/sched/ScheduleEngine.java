package com.kanven.practice.file.sched;

import com.kanven.practice.Configuration;
import com.kanven.practice.file.extension.DefaultExtensionLoader;
import com.kanven.practice.file.extension.Scope;
import com.kanven.practice.file.extension.Spi;
import com.kanven.practice.file.extension.SpiMate;
import com.kanven.practice.file.fetcher.Fetcher;
import com.kanven.practice.file.fetcher.FileEntry;
import com.kanven.practice.file.sched.executor.Executor;
import com.kanven.practice.file.sched.strategy.Strategy;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;

import static com.kanven.practice.Configuration.*;

@Slf4j
@Spi(scope = Scope.SINGLETON)
@SpiMate(name = "default")
public class ScheduleEngine<E> implements Closeable {

    private ExecutorService scheduler = Executors.newSingleThreadExecutor(r -> new Thread(r, "Schedule-Engine-Scheduler"));

    private volatile boolean init = false;

    public synchronized void start() {
        if (init) {
            return;
        }
        scheduler.submit(() -> {
            while (true) {
                try {
                    E entry = strategy().pickOut();
                    if (entry != null) {
                        executorService().submit(() -> executor().execute(entry));
                    }
                } catch (Exception e) {
                    log.error("", e);
                    break;
                }
            }
        });
        init = true;
    }

    @Override
    public synchronized void close() throws IOException {
        if (init) {
            scheduler.shutdown();
            executorService().shutdown();
            init = false;
        }
    }

    private ExecutorService executorService() {
        return new ThreadPoolExecutor(Configuration.getInteger(LEECH_SCHED_EXECUTOR_THREAD_CORE, 2),
                Configuration.getInteger(LEECH_SCHED_EXECUTOR_THREAD_MAX, 2), 0, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(20), r -> new Thread(r, "Schedule-Engine-Executor"));

    }

    private Strategy<E> strategy() {
        return (Strategy<E>) DefaultExtensionLoader.load(Strategy.class).getExtension(Configuration.getString(LEECH_SCHED_STRATEGY_NAME, "FIFO"));
    }

    private Executor<E> executor() {
        return (Executor<E>) DefaultExtensionLoader.load(Executor.class).getExtension(Configuration.getString(LEECH_EXECUTOR_MODE, "log"));
    }

    public static void main(String[] args) {
        new Fetcher();
        while (true){

        }
        //ScheduleEngine<FileEntry> engine = new ScheduleEngine<>();
        //engine.start();
    }

}
