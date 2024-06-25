package com.kanven.practice.file.sched.executor;

import com.kanven.practice.Configuration;
import com.kanven.practice.file.bulk.Listener;
import com.kanven.practice.file.extension.DefaultExtensionLoader;
import com.kanven.practice.file.extension.SpiMate;
import com.kanven.practice.file.fetcher.FileEntry;
import com.kanven.practice.file.sink.Sink;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.kanven.practice.Configuration.*;

@Slf4j
@SpiMate(name = "log")
public class LogExecutor implements Executor<FileEntry>, Listener {

    public static final String line_break = "\r\n";

    private Filter filter = DefaultExtensionLoader.load(Filter.class).getExtension(Configuration.getString(LEECH_EXECUTOR_LOG_FILTER, "text"), new ArrayList<Object>() {
        {
            add(Configuration.getString(LEECH_EXECUTOR_LOG_REGULAR));
        }
    });

    private Sink sink;

    private ConcurrentMap<File, String> contents = new ConcurrentHashMap<>(0);

    @Override
    public void execute(FileEntry entry) {
        log.info("file entry times:" + entry.times());
        entry.read(this);
    }

    @Override
    public void listen(Content content) {
        String date = contents.get(content.getFile());
        if (StringUtils.isNoneBlank(date)) {
            String line = content.getLine();
            //之前存在内容
            if (filter.filter(line)) {
                //新起一行
                System.out.println(date + line + line_break);
                contents.remove(content.getFile());
            }
            contents.put(content.getFile(), line + line_break);
        } else {
            //首次
            if (filter.filter(content.getLine())) {
                String line = content.getLine() + line_break;
                contents.put(content.getFile(), line);
            }
        }
    }

}
