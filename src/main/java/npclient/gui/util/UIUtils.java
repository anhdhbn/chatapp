package npclient.gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import npclient.CliConstants;
import npclient.core.callback.OnAcceptListener;
import npclient.core.callback.OnRejectListener;
import npclient.gui.manager.StageManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Optional;

public class UIUtils {

    public static <T extends Parent> T load(String path) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UIUtils.class.getResource(path));
            return fxmlLoader.load();
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

    public static InputStream retrieveAvatar(String name) throws IOException {
        String query = String.format("name=%s", URLEncoder.encode(name, CliConstants.CHARSET));
        String url = CliConstants.AVATAR_URL + query;
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/" +
                "537.11 (KHTML, like Gecko) Chrome/" +
                "23.0.1271.95 Safari/" +
                "537.11"
        );
        connection.connect();
        return connection.getInputStream();
    }
}
