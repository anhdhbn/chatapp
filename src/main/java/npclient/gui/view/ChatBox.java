package npclient.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import npclient.gui.controller.ChatBoxController;
import npclient.gui.entity.Messages;

import java.io.IOException;

public class ChatBox extends AnchorPane {

    private ChatBoxController controller;

    public ChatBox() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load(getClass().getResourceAsStream("/fxml/chatbox.fxml"));
            controller = fxmlLoader.getController();
            margin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void margin() {
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this,0.0);
    }

    public void setTarget(String title) {
        controller.setTitle(title);
    }

    public final String getTarget() {
        return controller.getTitle();
    }

    public void setItems(Messages messages) {
        controller.setItem(messages);
    }
}
