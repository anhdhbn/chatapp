package npclient.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

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
}
