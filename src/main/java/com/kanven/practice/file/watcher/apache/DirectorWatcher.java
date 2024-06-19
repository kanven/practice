package com.kanven.practice.file.watcher.apache;

import com.kanven.practice.file.watcher.DirectorWatcherComponent;
import com.kanven.practice.file.watcher.Watcher;
import com.kanven.practice.file.watcher.apache.DirectorListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class DirectorWatcher implements DirectorWatcherComponent {

    private final Path path;

    private final FileAlterationMonitor monitor;

    private final DirectorListener listener = new DirectorListener();

    public DirectorWatcher(String path) {
        this(path, -1);
    }

    public DirectorWatcher(String path, long interval) {
        this.path = Paths.get(path);
        this.monitor = interval > 0 ? new FileAlterationMonitor(interval) : new FileAlterationMonitor();
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        observer.addListener(listener);
        this.monitor.addObserver(observer);
    }

    public void start() {
        try {
            this.monitor.start();
        } catch (Exception e) {
            log.error("the director watcher start fail", e);
        }
    }


    public void stop() {
        try {
            this.monitor.stop();
        } catch (Exception e) {
            log.error("the director watcher stop fail", e);
        }
    }

    @Override
    public void listen(Watcher watcher) {
        this.listen(watcher);
    }

}
