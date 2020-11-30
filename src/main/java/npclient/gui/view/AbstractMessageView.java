package npclient.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import npclient.gui.entity.EmojiMessage;

import java.io.IOException;

public abstract class AbstractMessageView<T, C> extends VBox {

    private final FXMLLoader fxmlLoader;

    public AbstractMessageView(String path) {
        fxmlLoader = new FXMLLoader(getClass().getResource(path));
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected C getController() {
        return fxmlLoader.getController();
    }

    public abstract void setContent(T content);

    public void changeBackground(boolean isFromMe) {
        if (isFromMe) {
            changeTextColor(this, "white");
            setStyle("-fx-background-color: #5b61b9");
        } else {
            changeTextColor(this, "black");
            setStyle("-fx-background-color: #e3e6ea");
        }
    }

    private void changeTextColor(Node root, String color) {
        if (root instanceof Pane) {
            for (Node child : ((Pane) root).getChildren()) {
                changeTextColor(child, color);
            }
        } else if (root instanceof Text) {
            root.setStyle("-fx-fill: " + color);
        }
    }
}
