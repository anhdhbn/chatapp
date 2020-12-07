package npclient.gui.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import npclient.gui.manager.StageManager;
import npclient.gui.view.ImagePreview;
import npclient.gui.view.ImagePreviewStage;
import nputils.FileInfo;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ImageMessageController extends AbstractFileMessageController {

    @FXML
    private Rectangle rectThumbnail;
    @FXML
    private Button btnDownload;

    private Image image;
    private String name;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        rectThumbnail.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ImagePreviewStage stage = new ImagePreviewStage(image);
                stage.setTitle(name);
                stage.setOnDownloadListener(new ImagePreviewStage.OnDownloadListener() {
                    @Override
                    public void onDownload() {
                        ImageMessageController.this.onDownload();
                    }
                });
                stage.showAndWait();
            }
        });
    }

    @Override
    public void setFileInfo(FileInfo fileInfo) {
        super.setFileInfo(fileInfo);

        name = fileInfo.getName();
        image = new Image(new ByteArrayInputStream(fileInfo.getData()));
        final double HEIGHT = image.getHeight() / image.getWidth() * WIDTH;
        rectThumbnail.setHeight(HEIGHT);
        rectThumbnail.setWidth(WIDTH);
        ImagePattern pattern = new ImagePattern(image);
        rectThumbnail.setFill(pattern);
    }

    public void showDownloadButton() {
        btnDownload.setVisible(true);
    }

    public void hideDownloadButton() {
        btnDownload.setVisible(false);
    }

    private final static double WIDTH = 400.0f;
}
