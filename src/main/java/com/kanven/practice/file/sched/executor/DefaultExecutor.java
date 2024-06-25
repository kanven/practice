package com.kanven.practice.file.sched.executor;

import com.kanven.practice.file.bulk.Listener;
import com.kanven.practice.file.extension.SpiMate;
import com.kanven.practice.file.fetcher.FileEntry;
import com.kanven.practice.file.sink.Sink;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Slf4j
@SpiMate(name = "log")
public class DefaultExecutor implements Executor<FileEntry>, Listener {

    private Sink sink;

    private ConcurrentMap<File, String> contents = new ConcurrentHashMap<>(0);

    @Override
    public void execute(FileEntry entry) {
        log.info("file entry times:" + entry.times());
        entry.read(this);
    }

    @Override
    public void listen(Content content) {
        System.out.println(content.getLine());
    }

}
