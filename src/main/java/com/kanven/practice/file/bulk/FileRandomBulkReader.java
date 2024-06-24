package com.kanven.practice.file.bulk;

import com.kanven.practice.file.extension.SpiMate;

import java.io.*;
import java.nio.charset.Charset;

@SpiMate(name = "random")
public class FileRandomBulkReader extends BulkReader {

    public FileRandomBulkReader(File file, String charset) throws Exception {
        super(file, charset);
    }

    @Override
    public void read(Listener listener) throws Exception {
        this.size = this.raf.length();//size大小不准
        if (this.offset + 1 == this.size) {
            return;
        }
        //注意未成line时文件offset处理
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
                                long end = this.offset + line.size();
                                line.reset();
                                listener.listen(new Listener.Content(file, str, this.offset + 1, end));
                                this.offset = end;
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
                                    end = this.offset + line.size();
                                    line.reset();
                                    listener.listen(new Listener.Content(file, str, this.offset + 1, end));
                                }
                                line.write(b);
                                break;
                        }
                    }
                }
            }
        }
    }

}
