package com.kanven.practice.file.bulk;

import com.kanven.practice.Configuration;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

import static com.kanven.practice.Configuration.LEECH_BULK_FETCH_HISTORY;

public abstract class BulkReader implements Closeable {

    protected final RandomAccessFile raf;

    protected final Charset charset;

    protected long offset = 0;

    protected long size = -1;

    public BulkReader(File file, Charset charset) throws Exception {
        this.raf = new RandomAccessFile(file, "r");
        this.charset = charset;
        this.size = this.raf.length();
        boolean history = Configuration.getBoolean(LEECH_BULK_FETCH_HISTORY, false);
        if (!history) {
            offset = size;
        }
    }

    public abstract void read(Listener listener) throws Exception;

    public long delta() throws Exception {
        return this.raf.length() - offset;
    }

    public void close() throws IOException {
        this.raf.close();
    }

}
