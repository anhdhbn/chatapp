package npclient.gui.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import npclient.core.ChatItem;
import npclient.core.Message;

import java.io.IOException;

public abstract class MessageCell extends ListCell<Message> {

    private FXMLLoader mLLoader;
    @FXML
    private VBox container;
    @FXML
    private Text tContent;

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = generateCorrespondingLoader();
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            tContent.setText(item.getContent());

            setText(null);
            setGraphic(container);
        }
    }

    protected abstract FXMLLoader generateCorrespondingLoader();

}
