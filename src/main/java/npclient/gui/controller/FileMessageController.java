package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import nputils.FileInfo;

import java.io.InputStream;

public class FileMessageController extends AbstractFileMessageController {

    @FXML
    private Text tFileName;
    @FXML
    private Text tFileSize;
    @FXML
    private ImageView fileIcon;

    public void setIcon(InputStream url) {
        fileIcon.setImage(new Image(url));
    }

    public void setFileInfo(FileInfo fileInfo) {
        super.setFileInfo(fileInfo);
        this.tFileName.setText(fileInfo.getName());
        String fileSize = generateFileSize(fileInfo.getSize());
        this.tFileSize.setText(fileSize);
    }

    private static String generateFileSize(long size) {
        String unit = "bytes";
        if (size > 1024) {
            size /= 1024;
            unit = "KB";
        }
        if (size > 1024) {
            size /= 1024;
            unit = "MB";
        }

        return String.format("%d %s", size, unit);
    }
}
