package npclient.gui.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import npclient.gui.entity.FileMessage;
import npclient.gui.entity.Message;
import npclient.gui.entity.TextMessage;

import java.io.IOException;
import java.util.Date;

public class MessageCell extends ListCell<Message> {

    private FXMLLoader mLLoader;
    @FXML
    private VBox container;
    @FXML
    private Text tName, tTime;
    @FXML
    private AnchorPane paneContent;

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/message_container.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            tName.setText(item.getFrom());
            String time = new Date(item.getTime()).toString();
            tTime.setText(time);

            paneContent.getChildren().clear();
            if (item instanceof TextMessage) {
                TextMessageView messageView = new TextMessageView();
                messageView.setContent(((TextMessage) item).getContent());
                paneContent.getChildren().add(messageView);
            } else if (item instanceof FileMessage) {
                FileMessageView messageView = new FileMessageView();
                messageView.setContent(((FileMessage) item).getFileInfo());
                paneContent.getChildren().add(messageView);
            }

            setText(null);
            setGraphic(container);
        }
    }
}
