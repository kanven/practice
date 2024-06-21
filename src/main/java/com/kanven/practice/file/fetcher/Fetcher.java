package com.kanven.practice.file.fetcher;

import com.kanven.practice.Configuration;
import com.kanven.practice.file.bulk.BulkReader;
import com.kanven.practice.file.bulk.FileMMPBulkReader;
import com.kanven.practice.file.bulk.FileRandomBulkReader;
import com.kanven.practice.file.bulk.Listener;
import com.kanven.practice.file.fetcher.sched.Executor;
import com.kanven.practice.file.watcher.DirectorWatcher;
import com.kanven.practice.file.watcher.Event;
import com.kanven.practice.file.watcher.apache.ApacheDirectorWatcher;
import com.kanven.practice.file.watcher.jdk.NativeDirectorWatcher;
import com.kanven.practice.file.watcher.notify.NotifyWatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.kanven.practice.Configuration.*;

@Slf4j
public class Fetcher implements Executor<FileEntry> {

    private final CopyOnWriteArrayList<FileEntry> entries = new CopyOnWriteArrayList<>();

    private final CopyOnWriteArrayList<Listener> listeners = new CopyOnWriteArrayList<>();

    private final DirectorWatcher watcher;

    private final ListenerWrapper wrapper = new ListenerWrapper();

    public Fetcher() throws Exception {
        this.watcher = buildWatcher();
        this.init();
    }

    private void init() {
        watcher.listen(event -> {
            switch (event.getType()) {
                case NEW:
                    String path = event.getParent().toString() + File.separator + event.getChild();
                    File file = new File(path);
                    if (file.isFile()) {
                        try {
                            entries.add(new FileEntry(event.getParent().toString(), event.getChild().toString(), buildBulkReader(file)));
                        } catch (Exception e) {

                        }
                    }
                    break;
                case RENAME:
                    List<FileEntry> fes = filterEntries(event.getParent().toString(), event.getOld().toString(), entries);
                    fes.forEach(entry -> {
                        entry.setName(event.getChild().toString());
                    });
                    break;
                case MODIFY:
                    path = event.getParent().toString() + File.separator + event.getChild();
                    file = new File(path);
                    if (file.isFile()) {
                        fes = filterEntries(event, entries);
                        if (fes.isEmpty()) {
                            try {
                                FileEntry entry = new FileEntry(event.getParent().toString(), event.getChild().toString(), buildBulkReader(file));
                                entries.add(entry);
                                fes.add(entry);
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        }
                        fes.forEach(entry -> {
                            entry.increment();
                        });
                    }
                    break;
                case DELETED:
                    filterEntries(event, entries).forEach(entry -> {
                        entries.remove(entry);
                        entry.close();
                    });
                    break;
            }
        });
        watcher.start();
    }

    @Override
    public void execute(FileEntry entry) {
        try {
            entry.read(wrapper);
        } catch (Exception e) {

        }
    }

    private class ListenerWrapper implements Listener {

        @Override
        public void listen(String line) {
            listeners.forEach(listener -> {
                listener.listen(line);
            });
        }

    }

    private List<FileEntry> filterEntries(Event event, List<FileEntry> entries) {
        return entries.stream().filter(entry -> entry.getName().equals(event.getChild().toString())
                && entry.getDir().equals(event.getParent().toString())).collect(Collectors.toList());
    }

    private List<FileEntry> filterEntries(String dir, String name, List<FileEntry> entries) {
        return entries.stream().filter(entry -> entry.getDir().equals(name)
                && entry.getDir().equals(dir)).collect(Collectors.toList());
    }


    private DirectorWatcher buildWatcher() throws Exception {
        boolean recursion = Configuration.getBoolean(LEECH_DIR_RECURSION, false);
        String dir = Configuration.getString(LEECH_DIR, "");
        String name = Configuration.getString(LEECH_WATCHER_CLASS, NotifyWatcher.class.getName());
        if (NotifyWatcher.class.getName().equals(name)) {
            Class<NotifyWatcher> clazz = NotifyWatcher.class;
            Constructor<NotifyWatcher> constructor = clazz.getConstructor(String.class, Boolean.class);
            return constructor.newInstance(dir, recursion);
        } else if (ApacheDirectorWatcher.class.getName().equals(name)) {
            long interval = Configuration.getLong(LEECH_WATCHER_APACHE_INTERVAL, 1000L);
            Class<ApacheDirectorWatcher> clazz = ApacheDirectorWatcher.class;
            Constructor<ApacheDirectorWatcher> constructor = clazz.getConstructor(String.class, Long.class);
            return constructor.newInstance(dir, interval);
        } else if (NativeDirectorWatcher.class.getName().equals(name)) {
            Class<NativeDirectorWatcher> clazz = NativeDirectorWatcher.class;
            Constructor<NativeDirectorWatcher> constructor = clazz.getConstructor(String.class, Boolean.class);
            return constructor.newInstance(dir, recursion);
        }
        return null;
    }

    private BulkReader buildBulkReader(File file) throws Exception {
        String charset = Configuration.getString(LEECH_CHARSET, "UTF-8");
        String name = Configuration.getString(LEECH_BULK_READER_CLASS, FileMMPBulkReader.class.getName());
        if (FileMMPBulkReader.class.getName().equals(name)) {
            Class<FileMMPBulkReader> clazz = FileMMPBulkReader.class;
            Constructor<FileMMPBulkReader> constructor = clazz.getConstructor(File.class, Charset.class);
            return constructor.newInstance(file, Charset.forName(charset));
        } else if (FileRandomBulkReader.class.getName().equals(name)) {
            Class<FileRandomBulkReader> clazz = FileRandomBulkReader.class;
            Constructor<FileRandomBulkReader> constructor = clazz.getConstructor(File.class, Charset.class);
            return constructor.newInstance(file, Charset.forName(charset));
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        Fetcher fetcher = new Fetcher();
        while (true) {

        }
    }

}
