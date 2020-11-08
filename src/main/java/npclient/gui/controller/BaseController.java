package npclient.gui.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import npclient.MyAccount;
import npclient.core.callback.SubscribedTopicListener;
import npclient.core.command.Subscriber;
import npclient.core.entity.ChatItem;
import npclient.gui.manager.ChatBoxManager;
import npclient.gui.view.ChatBox;
import npclient.gui.view.ChatItemCell;
import nputils.Constants;
import nputils.DataTransfer;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BaseController implements Initializable {

    @FXML
    private AnchorPane paneChatbox;
    @FXML
    private ListView<ChatItem> lvChatItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ChatBoxManager.getInstance().register(paneChatbox);

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

        listenOnlineUsers();
    }

    private void listenOnlineUsers() {
        final String username = MyAccount.getInstance().getName();
        new Subscriber(Constants.ONLINE_TOPIC, username)
                .setNewMessageListener(new SubscribedTopicListener() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        String current = getCurrentChat();
                        boolean isCurrentOnline = current == null;

                        lvChatItem.getItems().clear();

                        List<String> onlineUsers = (List<String>) message.data;
                        for (String user : onlineUsers) {

                            if (!username.equals(user)) {
                                ChatItem item = new ChatItem();
                                item.setName(user);
                                lvChatItem.getItems().add(item);
                            }

                            if (user.equals(current))
                                isCurrentOnline = true;
                        }

                        if (!isCurrentOnline) {
                            clearChatBox();
                        }
                    }
                })
                .listen();
    }

    private String getCurrentChat() {
        if (paneChatbox.getChildren().isEmpty())
            return null;
        else {
            ChatBox chatBox = (ChatBox) paneChatbox.getChildren().get(0);
            return chatBox.getTarget();
        }
    }

    private void clearChatBox() {
        paneChatbox.getChildren().clear();
    }
}
