package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import npclient.gui.manager.StageManager;
import npclient.gui.util.UIUtils;
import nputils.FileInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class FileMessageController implements Initializable {

    @FXML
    private Text tFileName;
    @FXML
    private Text tFileSize;
    @FXML
    private ImageView fileIcon;

    private FileInfo fileInfo;

    private boolean isFromMe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setIcon(InputStream url) {
        fileIcon.setImage(new Image(url));
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
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

    @FXML
    public void onDownload() {
        if (!isFromMe) {
            if (fileInfo != null) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Save file");
                chooser.setInitialFileName(fileInfo.getName());
                Stage primaryStage = StageManager.getInstance().getPrimaryStage();
                File file = chooser.showSaveDialog(primaryStage);
                if (file != null) {
                    save(file);
                }
            } else {
                UIUtils.showErrorAlert("Can't find file's specification");
            }
        }
    }

    private void save(File file) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(fileInfo.getData());
            stream.close();
        } catch (IOException e) {
            UIUtils.showErrorAlert("Can't save file " + file.getName() + ": " + e.getMessage());
        }
    }

    public void setFromMe(boolean fromMe) {
        isFromMe = fromMe;
    }
}
