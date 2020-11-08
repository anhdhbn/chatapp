package npclient.gui.manager;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import npclient.gui.view.ChatBox;

public class ChatBoxManager {

    private static ChatBoxManager instance;

    private Pane container;

    public static ChatBoxManager getInstance() {
        if (instance == null)
            instance = new ChatBoxManager();
        return instance;
    }

    public void register(Pane container) {
        this.container = container;
    }

    public void clear() {
        container.getChildren().clear();
    }

    public final String current() {
        if (container.getChildren().isEmpty()) {
            return null;
        } else {
            ChatBox chatBox = (ChatBox) container.getChildren().get(0);
            return chatBox.getTarget();
        }
    }
}
