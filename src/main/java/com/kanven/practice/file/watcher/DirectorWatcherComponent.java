package com.kanven.practice.file.watcher;

public interface DirectorWatcherComponent {

    void start();

    void listen(Watcher watcher);

    void stop();

}
