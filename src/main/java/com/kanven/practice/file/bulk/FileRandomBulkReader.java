package com.kanven.practice.file.bulk;

import java.io.*;
import java.nio.charset.Charset;

public class FileRandomBulkReader extends BulkReader {

    public FileRandomBulkReader(File file, Charset charset) throws Exception {
        super(file, charset);
    }

    @Override
    public void read(Listener listener) throws Exception {
        this.size = this.raf.length();//size大小不准
        if (this.offset == this.size) {
            return;
        }
        while (this.offset < this.size) {
            this.raf.seek(this.offset);
            byte[] buffer = new byte[1024];
            while (this.raf.read(buffer) > 0) {
                try (ByteArrayOutputStream line = new ByteArrayOutputStream()) {
                    int len = buffer.length;
                    boolean seenCR = false;
                    for (int i = 0; i < len; i++) {
                        byte b = buffer[i];
                        switch (b) {
                            case '\n':
                                seenCR = false;
                                String str = new String(line.toByteArray(), charset);
                                line.reset();
                                listener.listen(str);
                                break;
                            case '\r':
                                if (seenCR) {
                                    line.write('\r');
                                }
                                seenCR = true;
                                break;
                            default:
                                if (seenCR) {
                                    seenCR = false;
                                    str = new String(line.toByteArray(), charset);
                                    line.reset();
                                    listener.listen(str);
                                }
                                line.write(b);
                                break;
                        }
                    }
                }
            }
            this.offset = this.raf.getFilePointer();
        }
    }

}
