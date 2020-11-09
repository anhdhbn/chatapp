package npclient.gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import npclient.core.callback.OnAcceptListener;
import npclient.core.callback.OnRejectListener;
import npclient.gui.manager.StageManager;

import java.io.IOException;
import java.util.Optional;

public class UIUtils {

    public static <T extends Parent> T load(String path) {
        try {
            return new FXMLLoader().load(UIUtils.class.getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showSimpleAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.initOwner(StageManager.getInstance().getPrimaryStage());
        alert.showAndWait();
    }

    public static void showErrorAlert(String error) {
        showSimpleAlert(Alert.AlertType.ERROR, error);
    }

    public static void showYesNoAlert(String content, OnAcceptListener aL, OnRejectListener rL) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
        alert.initOwner(StageManager.getInstance().getPrimaryStage());
        Optional<ButtonType> optional = alert.showAndWait();
        if (optional.get().equals(ButtonType.YES)) {
            aL.onAccept();
        } else {
            rL.onReject();
        }
    }
}
