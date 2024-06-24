package com.kanven.practice.file.sched.executor;

import com.kanven.practice.Configuration;
import com.kanven.practice.file.bulk.Listener;
import com.kanven.practice.file.extension.DefaultExtensionLoader;
import com.kanven.practice.file.extension.SpiMate;
import com.kanven.practice.file.fetcher.FileEntry;
import com.kanven.practice.file.sink.Sink;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.kanven.practice.Configuration.*;

@SpiMate(name = "log")
public class LogExecutor implements Executor<FileEntry>, Listener {

    public static final String line_break = "\r\n";

    private Filter filter = DefaultExtensionLoader.load(Filter.class).getExtension(Configuration.getString(LEECH_EXECUTOR_LOG_FILTER,"text"), new ArrayList<Object>() {
        {
            add(Configuration.getString(LEECH_EXECUTOR_LOG_REGULAR));
        }
    });

    private Sink sink;

    private ConcurrentMap<File, String> contents = new ConcurrentHashMap<>(0);

    @Override
    public void execute(FileEntry entry) {
        entry.read(this);
    }

    StringBuilder builder = new StringBuilder();

    @Override
    public void listen(Content content) {
        String line = contents.computeIfAbsent(content.getFile(), key -> "");
        if (filter.filter(content.getLine())) {
            if (StringUtils.isNoneBlank(line)) {
                // TODO 如何确保唯一消费（支持事务）？
                //undo
                System.out.println(line);
                //sink.sink(line);
                //do
            }
        }
        line += content.getLine() + line_break;
        contents.put(content.getFile(), line);
    }

}
