package npclient.gui.entity;

import nputils.FileInfo;

public class FileMessage extends Message {

    private FileInfo fileInfo;

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
