package com.kanven.practice.file.bulk;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public abstract class BulkReader implements Closeable {

    protected final RandomAccessFile raf;

    protected final Charset charset;

    protected long offset = 0;

    protected long size = -1;

    public BulkReader(File file, Charset charset) throws Exception {
        this.raf = new RandomAccessFile(file, "r");
        this.charset = charset;
    }

    public abstract void read(Listener listener) throws Exception;

    public void close() throws IOException {
        this.raf.close();
    }

}
