package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import npclient.core.transferable.FileInfo;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FileMessageController implements Initializable {

    @FXML
    private Text tFileName;
    @FXML
    private Text tFileSize;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setFileInfo(FileInfo fileInfo) {
        this.tFileName.setText(fileInfo.getName());

        String unit = "byte";
        long size = fileInfo.getSize();
        if (size > 1024) {
            size /= 1024;
            unit = "KB";
        }
        if (size > 1024) {
            size /= 1024;
            unit = "MB";
        }
        this.tFileSize.setText(String.format("%d %s", size, unit));
    }
}
