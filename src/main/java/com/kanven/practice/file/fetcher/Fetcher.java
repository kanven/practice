package com.kanven.practice.file.fetcher;

import com.kanven.practice.file.bulk.BulkReader;
import com.kanven.practice.file.bulk.FileMMPBulkReader;
import com.kanven.practice.file.watcher.apache.ApacheDirectorWatcher;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Fetcher {

    public static void main(String[] args) throws Exception {
        String dir = "/Applications/metric/logs";
        List<FileEntry> entries = new ArrayList<>(0);
        try (ApacheDirectorWatcher watcher = new ApacheDirectorWatcher(dir)) {
            watcher.listen(event -> {
                switch (event.getType()) {
                    case NEW:
                        String path = event.getParent().toString() + File.separator + event.getChild();
                        File file = new File(path);
                        if (file.isFile()) {
                            try {
                                BulkReader reader = new FileMMPBulkReader(file, Charset.forName("UTF-8"));
                                entries.add(new FileEntry(event.getParent().toString(), event.getChild().toString(), reader));
                            } catch (Exception e) {

                            }
                        }
                        break;
                    case MODIFY:

                        break;
                    case DELETED:
                        List<FileEntry> fes = entries.stream().filter(entry -> entry.name.equals(event.getChild().toString())
                                && entry.dir.equals(event.getParent().toString())).collect(Collectors.toList());
                        fes.forEach(entry -> {
                            try {
                                entries.remove(entry);
                                entry.reader.close();
                            } catch (Exception e) {

                            }
                        });
                        break;
                }
            });
        }
    }

    private static class FileEntry {

        private String dir;

        private String name;

        private BulkReader reader;

        public FileEntry(String dir, String name, BulkReader reader) {
            this.dir = dir;
            this.name = name;
            this.reader = reader;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FileEntry fileEntry = (FileEntry) o;
            return Objects.equals(dir, fileEntry.dir) &&
                    Objects.equals(name, fileEntry.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dir, name);
        }
    }

}
