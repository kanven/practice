package com.kanven.practice.file.bulk;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileMMPBulkReader extends BulkReader {

    private final FileChannel channel;

    public FileMMPBulkReader(File file, Charset charset) throws Exception {
        super(file, charset);
        this.channel = this.raf.getChannel();
        this.size = this.channel.size();
    }

    public void read(Listener listener) throws Exception {
        this.size = this.channel.size();
        if (offset == this.size) {
            return;
        }
        long delta = offset == 0 ? this.size : this.size - offset;
        int page = (int) (delta / Integer.MAX_VALUE);
        if (delta % Integer.MAX_VALUE != 0) {
            page += 1;
        }
        for (long p = 1; p <= page; p++) {
            long pageSize = p * Integer.MAX_VALUE > delta ? delta - (p - 1) * Integer.MAX_VALUE : Integer.MAX_VALUE;
            MappedByteBuffer buffer = this.channel.map(FileChannel.MapMode.READ_ONLY, offset, pageSize);
            try (ByteArrayOutputStream line = new ByteArrayOutputStream()) {
                int cap = buffer.capacity();
                boolean seenCR = false;
                int pos = 0;
                for (int i = 0; i < cap; i++) {
                    byte b = buffer.get(i);
                    switch (b) {
                        case '\n':
                            seenCR = false;
                            String str = new String(line.toByteArray(), charset);
                            line.reset();
                            listener.listen(str);
                            offset += i - pos;
                            pos = i;
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
                                offset += i - pos;
                                pos = i;
                            }
                            line.write(b);
                            break;
                    }
                }
            }
        }
        this.offset += 1;
    }

}
