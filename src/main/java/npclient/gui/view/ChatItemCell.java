package npclient.gui.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private Text tLastMsg;
    @FXML
    private CircleImageView civAvatar;

    private String prev;

    @Override
    protected void updateItem(ChatItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/chat_item.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (item.getLastMessage() == null) {
                tLastMsg.setText("Tap and wave hand with your friend");
                tLastMsg.setOpacity(0.6f);
            } else {
                tLastMsg.setText(item.getLastMessage());
                tLastMsg.setOpacity(1f);
            }

            String name = item.getName();
            if (!name.equals(prev)) {
                tTitle.setText(item.getName());
                civAvatar.update(item.getName());
            }
            prev = name;

            setText(null);
            setGraphic(container);
        }

    }

}
