package npclient.gui.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import npclient.gui.StageManager;

import java.io.IOException;

public class FXMLUtils {

    public static Parent load(String path) {
        try {
            return new FXMLLoader().load(FXMLUtils.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return new Pane();
        }
    }

    public static void showSimpleAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.initOwner(StageManager.getInstance().getPrimaryStage());
        alert.showAndWait();
    }
}
