package npclient.gui.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import npclient.gui.entity.ChatItem;
import npclient.gui.entity.GroupChatItem;
import npclient.gui.manager.StageManager;

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
    @FXML
    private Circle cirSeen;

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

        cirSeen.setVisible(!item.isSeen());
        if (item.isSeen()) {
            tLastMsg.setStyle("-fx-font-family: meRegular;");
        } else {
            tLastMsg.setStyle("-fx-font-family: meBold;");
        }

        if (item instanceof GroupChatItem) {
            MenuItem menuItem = new MenuItem("Leave");
            menuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    StageManager.getInstance().getBaseController().leave(name);
                }
            });
            ContextMenu menu = new ContextMenu(menuItem);
            setContextMenu(menu);
        } else {
            setContextMenu(null);
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
