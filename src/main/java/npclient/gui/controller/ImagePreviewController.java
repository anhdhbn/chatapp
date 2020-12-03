package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ImagePreviewController implements Initializable {

    @FXML
    private Slider hScroll;
    @FXML
    private Slider vScroll;
    @FXML
    private ImageView ivImage;
    @FXML
    private Slider sldZoom;
    @FXML
    private Label lZoomValue;

    private double screenWidth, screenHeight;
    private double offsetX, offsetY, zoomLvl;
    private double initX, initY;
    private double width, height;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivImage.setCursor(Cursor.OPEN_HAND);

        sldZoom.valueProperty().addListener(e -> {
            zoomLvl = sldZoom.getValue();
            lZoomValue.setText(String.format("%.2f", zoomLvl));
            normalizeOffset();
            hScroll.setValue(offsetX);
            vScroll.setValue(height - offsetY);
            renderImage();
        });

        vScroll.setMin(0);
        vScroll.valueProperty().addListener(e -> {
            offsetY = height - vScroll.getValue();
            zoomLvl = sldZoom.getValue();
            normalizeOffset();
            renderImage();
        });

        hScroll.setMin(0);
        hScroll.valueProperty().addListener(e -> {
            offsetX = hScroll.getValue();
            zoomLvl = sldZoom.getValue();
            normalizeOffset();
            renderImage();
        });
    }

    public void setImage(Image image) {
        ivImage.setImage(image);

        ivImage.setPreserveRatio(false);
        ivImage.setFitWidth(screenWidth);
        ivImage.setFitHeight(screenHeight);

        width = image.getWidth();
        height = image.getHeight();

        offsetX = width / 2;
        offsetY = height / 2;

        hScroll.setMax(width);

        vScroll.setMax(height);
    }

    private void normalizeOffset() {
        if (offsetX < (width / zoomLvl) / 2) {
            offsetX = (width / zoomLvl) / 2;
        }
        if (offsetX > width - (width / zoomLvl / 2)) {
            offsetX = width - (width / zoomLvl / 2);
        }
        if (offsetY < (height / zoomLvl / 2)) {
            offsetY = height / zoomLvl / 2;
        }
        if (offsetY > height - (height / zoomLvl / 2)) {
            offsetY = height - (height / zoomLvl / 2);
        }
    }

    private void renderImage() {
        ivImage.setViewport(new Rectangle2D(
                offsetX - (width / zoomLvl / 2),
                offsetY - (height / zoomLvl / 2),
                width / zoomLvl,
                height / zoomLvl
        ));
    }

    @FXML
    public void onImageDragged(MouseEvent e) {
        hScroll.setValue(hScroll.getValue() + (initX - e.getSceneX()));
        vScroll.setValue(vScroll.getValue() - (initY - e.getSceneY()));
        initX = e.getSceneX();
        initY = e.getSceneY();
    }

    @FXML
    public void onImageReleased(MouseEvent e) {
        ivImage.setCursor(Cursor.OPEN_HAND);
    }

    @FXML
    public void onImagePressed(MouseEvent e) {
        initX = e.getSceneX();
        initY = e.getSceneY();
        ivImage.setCursor(Cursor.CLOSED_HAND);
    }

    @FXML
    public void onImageClicked(MouseEvent e) {
        if (e.getButton().equals(MouseButton.PRIMARY)) {
            // double click
            if (e.getClickCount() == 2){
                final double medium = (sldZoom.getMax() + sldZoom.getMin()) / 2;
                if (sldZoom.getValue() < medium)
                    sldZoom.setValue(sldZoom.getMax());
                else
                    sldZoom.setValue(sldZoom.getMin());
            } else
                e.consume();
        }
    }

    public void setScreenSize(double w, double h) {
        this.screenWidth = w;
        this.screenHeight = h;
    }
}
