package npclient.gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import npclient.CliConstants;
import npclient.core.callback.OnAcceptListener;
import npclient.core.callback.OnRejectListener;
import npclient.gui.manager.StageManager;
import nputils.Emoji;

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
        String nameEncode = URLEncoder.encode(name.replaceAll("\\s+", ""), CliConstants.CHARSET);
        String background = mapNameToColor(name);
        String url = String.format(CliConstants.AVATAR_URL, background, nameEncode);
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

    private static String mapNameToColor(String name) {
        final float BOUND = 26 + 10;

        char[] nameBuf = name.toCharArray();

        byte[] colorBuf = new byte[3];
        colorBuf[2] = (byte) (name.hashCode()  % Character.MAX_VALUE);

        for (int i = 0; i < colorBuf.length - 1;) {
            if (i < nameBuf.length) {
                byte b;
                char c = nameBuf[i];
                if (Character.isDigit(c)) {
                    b = (byte) ((c - '0') / BOUND * Character.MAX_VALUE);
                } else if (Character.isAlphabetic(nameBuf[i])) {
                    char lower = Character.toLowerCase(nameBuf[i]);
                    b = (byte) ((lower - 'a' + 9) / BOUND * Character.MAX_VALUE);
                } else b = (byte) c;
                colorBuf[i] = b;
                i++;
            } else
                break;
        }

        StringBuilder sb = new StringBuilder(colorBuf.length * 2);
        for (byte b : colorBuf){
            sb.append(String.format("%02x", b&0xff));
        }

        return sb.toString();
    }

    public static boolean isInvalid(String name) {
        final String trimName = name.trim();
        return trimName.isEmpty() || !trimName.matches("[\\w_\\s]{3,255}");
    }

    public static void main(String[] args) {
        System.out.println(mapNameToColor("Lamnt"));
    }

    public static class Emoji {

        public static Image toImage(nputils.Emoji emoji) {
            String name = emoji.name().substring(1);
            String filePath = String.format("/img/emoji/%s.gif", name);
            filePath = filePath.replace("_", "-");
//            filePath = "/img/emoji/face_throwing_a_kiss_256_1.gif";
            return new Image(filePath);
        }

    }
}
