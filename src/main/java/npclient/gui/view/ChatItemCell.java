package npclient.gui.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import npclient.gui.entity.ChatItem;

import java.io.IOException;

public class ChatItemCell extends ListCell<ChatItem> {

    private FXMLLoader mLLoader;
    @FXML
    private HBox container;
    @FXML
    private Text tTitle;
    @FXML
    private Label tLastMsg;
    @FXML
    private CircleImageView civAvatar;

    private String prevName;
    private String prevMsg;

    @Override
    protected void updateItem(ChatItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);

        } else {
            item.setCell(this);

            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/chat_item.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            updateItem(item);
        }

    }

    public void updateItem(ChatItem item) {
        String name = item.getName();
        String lastMsg = item.getLastMessage();

        if (!name.equals(prevName)) {
            updateLastMsg(lastMsg);
            tTitle.setText(item.getName());
            civAvatar.update(item.getName());
        } else if (shouldUpdateLastMsg(lastMsg)) {
            updateLastMsg(lastMsg);
        }

        this.prevName = name;
        this.prevMsg = item.getLastMessage();

        setText(null);
        setGraphic(container);
    }

    private boolean shouldUpdateLastMsg(String lastMsg) {
        if (lastMsg == null && prevMsg == null)
            return false;

        if (lastMsg == null)
            return true;

        return !lastMsg.equals(prevMsg);
    }

    private void updateLastMsg(String lastMsg) {
        if (lastMsg == null) {
            tLastMsg.setText("Tap and wave hand with your friend");
            tLastMsg.setOpacity(0.6f);
        } else {
            tLastMsg.setText(lastMsg);
            tLastMsg.setOpacity(1f);
        }
    }
}
