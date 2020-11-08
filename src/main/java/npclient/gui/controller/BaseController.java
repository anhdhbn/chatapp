package npclient.gui.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import npclient.MyAccount;
import npclient.core.callback.SubscribedTopicListener;
import npclient.core.command.Subscriber;
import npclient.core.data.MessageManager;
import npclient.core.entity.ChatItem;
import npclient.core.entity.Message;
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
        lvChatItem.setCellFactory(new Callback<ListView<ChatItem>, ListCell<ChatItem>>() {
            @Override
            public ListCell<ChatItem> call(ListView<ChatItem> param) {
                return new ChatItemCell();
            }
        });

        lvChatItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ChatItem>() {
            @Override
            public void changed(ObservableValue<? extends ChatItem> observable, ChatItem oldChat, ChatItem newChat) {
                if (newChat != null)
                    changeChatBox(newChat.getName());
            }
        });

        listenOnlineUsers();
    }

    /**
     * Listen for online users
     */
    private void listenOnlineUsers() {
        final String username = MyAccount.getInstance().getName();
        new Subscriber(Constants.ONLINE_TOPIC, username)
                .setNewMessageListener(new SubscribedTopicListener() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        // Get current user in chat box
                        String current = getCurrentChat();
                        boolean isCurrentOnline = current == null;

                        // Clear all exist chat item
                        lvChatItem.getItems().clear();

                        // Retrieve online users
                        List<String> onlineUsers = (List<String>) message.data;

                        // Clear all offline user chat messages in MessageManager
                        MessageManager.getInstance().clearOffline(onlineUsers);

                        for (String user : onlineUsers) {
                            // Add user (not your self) into listview
                            if (!username.equals(user)) {
                                ChatItem item = new ChatItem();
                                item.setName(user);
                                lvChatItem.getItems().add(item);

                                // Check whether current user still online
                                if (user.equals(current))
                                    isCurrentOnline = true;
                                else {
                                    // with other user listen message
                                    subscribeMessages(username, user);
                                }
                            }
                        }

                        // In case current user offline
                        // Clear chat box
                        if (!isCurrentOnline) {
                            clearChatBox();
                        }
                    }
                })
                .listen();
    }

    private void subscribeMessages(String username, String target) {
        final String topic = String.format("chat/%s", target);
        new Subscriber(topic, username)
                .setNewMessageListener(new SubscribedTopicListener() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        Object content = message.data;
                        if (content instanceof String) {
                            Message m = new Message();
                            m.setFrom(message.name);
                            m.setTime(message.datetime);
                            m.setContent(content.toString());
                            MessageManager.getInstance().append(target, m);
                        }
                    }
                })
                .listen();
    }

    /**
     * Get current chatting user
     * @return current chatting username
     */
    private String getCurrentChat() {
        if (!paneChatbox.getChildren().isEmpty()) {
            Node first = paneChatbox.getChildren().get(0);
            if (first instanceof ChatBox) {
                return ((ChatBox) first).getTarget();
            }
        }

        return null;
    }

    /**
     * Clear chat box section
     */
    private void clearChatBox() {
        paneChatbox.getChildren().clear();
    }

    /**
     * Change chat box section by username
     * @param target username
     */
    private void changeChatBox(String target) {
        ChatBox chatBox = new ChatBox();
        chatBox.setTarget(target);

        clearChatBox();
        paneChatbox.getChildren().add(chatBox);
    }
}
