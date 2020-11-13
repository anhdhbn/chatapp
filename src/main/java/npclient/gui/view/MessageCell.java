package npclient.gui.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import npclient.MyAccount;
import npclient.gui.entity.Message;
import npclient.gui.util.DateTimeUtils;
import nputils.Emoji;
import nputils.FileInfo;

import java.io.IOException;

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

            setSender(item.getFrom());
            setTime(item.getTime());
            setContent(item.getContent());

            final String username = MyAccount.getInstance().getName();
            boolean isFromMe = item.getFrom().equals(username);
            setMessageAlignment(isFromMe);

            setText(null);
            setGraphic(container);
        }
    }

    private void setMessageAlignment(boolean isFromMe) {
        if (isFromMe) {
            container.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            tName.setVisible(false);
        } else {
            container.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    private void setContent(Object content) {
        paneContent.getChildren().clear();
        if (content instanceof String) {
            TextMessageView messageView = new TextMessageView();
            messageView.setContent((String) content);
            paneContent.getChildren().add(messageView);
        } else if (content instanceof FileInfo) {
            FileMessageView messageView = new FileMessageView();
            messageView.setContent((FileInfo) content);
            paneContent.getChildren().add(messageView);
        } else if (content instanceof Emoji) {
            EmojiView messageView = new EmojiView();
            messageView.setContent((Emoji) content);
            paneContent.getChildren().add(messageView);
        }
    }

    private void setSender(String name) {
        tName.setText(name);
    }

    private void setTime(long time) {
        String text = DateTimeUtils.format(time);
        tTime.setText(text);
    }
}
