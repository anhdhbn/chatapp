package npclient.gui.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import npclient.core.ChatItem;
import npclient.gui.view.ChatBox;
import npclient.gui.view.ChatItemCell;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class BaseController implements Initializable {

    @FXML
    private AnchorPane paneChatbox;
    @FXML
    private ListView<ChatItem> lvChatItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lvChatItem.setCellFactory(new Callback<ListView<ChatItem>, ListCell<ChatItem>>() {
            @Override
            public ListCell<ChatItem> call(ListView<ChatItem> param) {
                return new ChatItemCell();
            }
        });

        lvChatItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChatItem>() {
            @Override
            public void changed(ObservableValue<? extends ChatItem> observable, ChatItem oldValue, ChatItem newValue) {
                ChatBox chatBox = new ChatBox();
                chatBox.setTarget(newValue.getName());

                paneChatbox.getChildren().clear();
                paneChatbox.getChildren().add(chatBox);
            }
        });

        generateRandom();
    }

    private void generateRandom() {
        int users = new Random().nextInt(8);
        for (int i = 0; i < users; i++) {
            ChatItem chat = new ChatItem();
            byte[] buf = new byte[32];
            new Random().nextBytes(buf);
            chat.setName(new String(buf));
            byte[] bufMsg = new byte[64];
            new Random().nextBytes(bufMsg);
            chat.setLastMessage(new String(bufMsg));
            lvChatItem.getItems().add(chat);
        }
    }
}
