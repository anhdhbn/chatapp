package npclient.gui.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import npclient.MyAccount;
import npclient.gui.entity.Message;
import npclient.gui.util.DateTimeUtils;
import npclient.gui.util.UIUtils;
import nputils.Emoji;
import nputils.FileInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MessageCell extends ListCell<Message> implements Initializable {

    private FXMLLoader mLLoader;
    @FXML
    private VBox container;
    @FXML
    private Text tName, tTime;
    @FXML
    private AnchorPane paneContent;

    private Object content;
    private AbstractMessageView<?,?> messageView;

    private final Text tState = new Text();

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

            final String username = MyAccount.getInstance().getName();
            boolean isFromMe = item.getFrom().equals(username);

            setSender(item.getFrom());
            setTime(item.getTime());
            content = item.getContent();
            setContent();
            setState(item.getState());

            setMessageAlignment(isFromMe);

            setText(null);
            setGraphic(container);
        }
    }

    private void setState(Message.State state) {
        switch (state) {
            case SUCCESS:
                tState.setVisible(false);
                break;

            case FAILURE:
                tState.setText("Error!");
                tState.setVisible(true);
                break;

            case SENDING:
                tState.setText("Sending...");
                tState.setVisible(true);
                break;
        }
    }

    private void setMessageAlignment(boolean isFromMe) {
        if (isFromMe) {
            container.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            tName.setVisible(false);
            tName.setFont(new Font(0));

        } else {
            container.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            tName.setFont(new Font(12));
            tName.setVisible(true);
        }

        if (messageView != null)
            messageView.changeBackground(isFromMe);
    }

    private void setContent() {
        paneContent.getChildren().clear();
        if (content instanceof String) {
            TextMessageView messageView = new TextMessageView();
            messageView.setContent((String) content);
            this.messageView = messageView;
            messageView.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            paneContent.getChildren().add(messageView);
        } else if (content instanceof FileInfo) {
            FileInfo info = (FileInfo) content;
            String fileName = info.getName();
            if (UIUtils.isImage(fileName)) {
                ImageMessageView messageView = new ImageMessageView();
                messageView.setContent(info);
                this.messageView = messageView;
            } else {
                FileMessageView messageView = new FileMessageView();
                messageView.setContent(info);
                this.messageView = messageView;
            }
            messageView.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            paneContent.getChildren().add(messageView);
        } else if (content instanceof Emoji) {
            EmojiView messageView = new EmojiView();
            messageView.setContent((Emoji) content);
            this.messageView = messageView;
            messageView.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tState.setFont(Font.font(null, FontPosture.ITALIC, 10));
        tState.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        container.getChildren().add(tState);
    }
}
