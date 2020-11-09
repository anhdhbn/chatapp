package npclient.core.transferable;

import npclient.core.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;

public class FileInfo implements Serializable {

    private byte[] data;
    private String md5;
    private String name;
    private long size;

    public FileInfo() {

    }

    public FileInfo(File file) throws IOException, NoSuchAlgorithmException {
        if (file == null)
            throw new FileNotFoundException();

        this.size = file.length();
        this.name = file.getName();
        this.data = Files.readAllBytes(file.toPath());
        this.md5 = Utils.computeMd5(file);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
