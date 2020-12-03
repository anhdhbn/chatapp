package npclient.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import npclient.gui.controller.ImagePreviewController;

import java.io.IOException;

public class ImagePreview extends StackPane {

    private ImagePreviewController controller;

    public ImagePreview() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/image_preview.fxml"));
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setScreenSize(double w, double h) {
        controller.setScreenSize(w, h);
    }

    public void setImage(Image image) {
        controller.setImage(image);
    }
}
