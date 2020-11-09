package npclient.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public abstract class AbstractMessageView<T, C> extends VBox {

    private FXMLLoader fxmlLoader;

    public AbstractMessageView(String path) {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected C getController() {
        return fxmlLoader.getController();
    }

    public abstract void setContent(T content);
}
