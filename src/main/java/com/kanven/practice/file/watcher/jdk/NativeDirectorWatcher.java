package com.kanven.practice.file.watcher.jdk;


import com.kanven.practice.file.watcher.DirectorWatcherComponent;
import com.kanven.practice.file.watcher.Event;
import com.kanven.practice.file.watcher.Watcher;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <ul>jdk原生WatchService存在一下问题：
 * <li>1、延迟问题，底层PollingWatchService实现采用的是定时任务获取事件;</li>
 * <li>2、不监听子目录下事件</li>
 * </ul>
 *
 * @author kanven
 */
@Slf4j
public class NativeDirectorWatcher implements DirectorWatcherComponent {

    private final String path;

    private final boolean recursion;

    private final AtomicInteger state = new AtomicInteger(State.INITED.state);

    private final List<Path> paths = new ArrayList<>(0);

    private final Map<Path, WatchService> watchers = new HashMap<>(0);

    private final ExecutorService executor = Executors.newSingleThreadExecutor((r) -> {
        Thread t = new Thread(r, "Thread-Director-Watcher");
        t.setDaemon(true);
        return t;
    });

    private final List<Watcher> handlers = new ArrayList<>(0);

    private final ReentrantLock lock = new ReentrantLock();

    public NativeDirectorWatcher(String path, boolean recursion) {
        this.path = path;
        this.recursion = recursion;
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException(path + " 不存在");
        }
        if (!file.isDirectory()) {
            throw new RuntimeException(path + " 不是目录");
        }
        this.paths.add(Paths.get(this.path));
        if (recursion) {
            this.paths.addAll(fetch(new ArrayList<File>() {{
                add(file);
            }}));
        }
    }

    private List<Path> fetch(List<File> dirs) {
        if (dirs == null || dirs.isEmpty()) {
            return new ArrayList<>(0);
        }
        List<Path> paths = new ArrayList<>(0);
        List<File> second = new ArrayList<>(0);
        dirs.forEach(file -> {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isDirectory()) {
                        paths.add(Paths.get(child.getPath()));
                        second.add(child);
                    }
                }
            }
        });
        paths.addAll(fetch(second));
        return paths;
    }

    @Override
    public void start() {
        lock.lock();
        try {
            if (state.get() >= State.STARTING.state) {
                return;
            }
            state.compareAndSet(State.INITED.state, State.STARTING.state);
        } finally {
            lock.unlock();
        }
        paths.forEach(path -> createWatchService(path));
        executor.submit(() -> {
            Iterator<Map.Entry<Path, WatchService>> iterator = this.watchers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Path, WatchService> entry = iterator.next();
                WatchService watcher = entry.getValue();
                Path path = entry.getKey();
                WatchKey key = watcher.poll();
                if (key != null) {
                    List<WatchEvent<?>> events = key.pollEvents();
                    events.forEach(event -> {
                        if (event.kind() != StandardWatchEventKinds.OVERFLOW) {
                            Event en = new Event();
                            Path child = (Path) event.context();
                            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                                en.setType(Event.EventType.NEW);
                                Path np = Paths.get(path.toString() + File.separator + child.toString());
                                if (recursion) {
                                    List<Path> paths = fetch(new ArrayList<File>() {{
                                        add(path.toFile());
                                    }});
                                    paths.add(np);
                                    paths.forEach(p -> createWatchService(p));
                                }
                            } else if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                                en.setType(Event.EventType.DELETED);
                                paths.remove(path);
                                iterator.remove();
                            } else {
                                en.setType(Event.EventType.MODIFY);
                            }
                            en.setParent(path);
                            en.setChild(child);
                            Iterator<Watcher> itr = handlers.iterator();
                            while (itr.hasNext()) {
                                Watcher w = itr.next();
                                try {
                                    w.watcher(en);
                                } catch (Exception e) {
                                    log.error("the handler throw an exception!", e);
                                }
                            }
                        }
                    });
                    if (!key.reset()) {
                        createWatchService(path);
                    }
                }
            }
        });
        lock.lock();
        try {
            state.compareAndSet(State.STARTING.state, State.STARTED.state);
            lock.notifyAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void listen(Watcher watcher) {
        if (state.get() <= State.STARTED.state) {
            this.handlers.add(watcher);
        }
    }

    @Override
    public void stop() {
        lock.lock();
        try {
            if (state.get() >= State.STOPPING.state) {
                return;
            }
            if (state.get() == State.STARTING.state) {
                try {
                    lock.wait();
                } catch (Exception e) {
                    log.error("lock wait occur an exception", e);
                }
            }
            state.compareAndSet(State.STARTED.state, State.STOPPING.state);
        } finally {
            lock.unlock();
        }
        this.watchers.forEach((path, watcher) -> {
            try {
                watcher.close();
            } catch (Exception e) {
                log.error(path.toString() + "'s watcher close action occur an exception!", e);
            }
        });
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
        state.compareAndSet(State.STOPPING.state, State.STOPPED.state);
    }

    private void createWatchService(Path path) {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            watchers.put(path, watcher);
        } catch (IOException e) {
            log.error(path.toString() + " create watcher occur an exception!", e);
        }
    }


    enum State {
        INITED(0),
        STARTING(1),
        STARTED(2),
        STOPPING(3),
        STOPPED(4);

        private int state;

        State(int state) {
            this.state = state;
        }

    }

}
