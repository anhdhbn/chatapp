package nputils;

import java.io.*;

public class Attachment {

    private String name;
    private String md5;
    private byte[] buffer;
    private long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public File save(String path) throws IOException {
        OutputStream out = new FileOutputStream(path);

        out.write(buffer, 0, buffer.length);
        out.close();

        return new File(path);
    }
}
