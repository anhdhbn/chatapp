package npclient.gui.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import npclient.MyAccount;
import npclient.core.callback.OnAcceptListener;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.callback.OnRejectListener;
import npclient.core.callback.SubscribedTopicListener;
import npclient.core.command.Publisher;
import npclient.core.command.Subscriber;
import npclient.gui.manager.MessageManager;
import npclient.gui.entity.ChatItem;
import npclient.gui.entity.TextMessage;
import npclient.gui.manager.StageManager;
import npclient.gui.util.UIUtils;
import npclient.gui.view.ChatBox;
import npclient.gui.view.ChatItemCell;
import nputils.Constants;
import nputils.DataTransfer;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BaseController implements Initializable {

    @FXML
    private AnchorPane paneChatbox;
    @FXML
    private ListView<ChatItem> lvChatItem;

    private Stage voiceChatStage;

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

        listenVoiceCall();
    }

    private void listenVoiceCall() {
        final String username = MyAccount.getInstance().getName();
        final String topic = String.format("voice/%s", username);
        new Subscriber(topic, username)
                .setNewMessageListener(new SubscribedTopicListener() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        String action = message.data.toString();
                        switch (action) {
                            case Constants.VOICE_REQUEST:
                                onReceiveVoiceRequest(message);
                                break;

                            case Constants.VOICE_ACCEPT:
                                onReceiveVoiceAccept(message);
                                break;

                            case Constants.VOICE_REJECT:
                                onReceiveVoiceReject(message);
                                break;

                            case Constants.VOICE_QUIT:
                                onReceiveVoiceQuit(message);

                        }
                    }
                })
                .listen();
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
                            TextMessage m = new TextMessage();
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

    private void onReceiveVoiceQuit(DataTransfer message) {
        closeVoiceChatDialog();
    }

    private void onReceiveVoiceReject(DataTransfer message) {
        MyAccount.getInstance().setInCall(false);
    }

    private void onReceiveVoiceAccept(DataTransfer message) {
        openVoiceChatDialog(message.name);
    }

    private void onReceiveVoiceRequest(DataTransfer message) {
        boolean inCall = MyAccount.getInstance().isInCall();
        final String username = MyAccount.getInstance().getName();
        final String resTopic = String.format("voice/%s", message.name);

        if (inCall) {
            new Publisher(resTopic, username)
                    .putData(Constants.VOICE_REJECT)
                    .post();
        } else {
            MyAccount.getInstance().setInCall(true);
            String content = String.format("%s is calling you. Answer?", message.name);
            UIUtils.showYesNoAlert(content, new OnAcceptListener() {
                @Override
                public void onAccept() {
                    new Publisher(resTopic, username)
                            .putData(Constants.VOICE_ACCEPT)
                            .setSuccessListener(new OnPublishMessageSuccess() {
                                @Override
                                public void onReceive(DataTransfer message) {
                                    openVoiceChatDialog(message.name);
                                }
                            })
                            .post();
                }
            }, new OnRejectListener() {
                @Override
                public void onReject() {
                    new Publisher(resTopic, username)
                            .putData(Constants.VOICE_REJECT)
                            .setSuccessListener(new OnPublishMessageSuccess() {
                                @Override
                                public void onReceive(DataTransfer message) {
                                    MyAccount.getInstance().setInCall(false);
                                }
                            })
                            .post();
                }
            });
        }
    }

    private void openVoiceChatDialog(String target) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/voice_chat.fxml"));
            Parent root = loader.load();
            VoiceChatController controller = loader.getController();
            controller.setUser1(MyAccount.getInstance().getName());
            controller.setUser2(target);

            Scene scene = new Scene(root);

            voiceChatStage = new Stage();
            voiceChatStage.setTitle("Voice Chat");
            voiceChatStage.setScene(scene);

            voiceChatStage.initOwner(StageManager.getInstance().getPrimaryStage());

            voiceChatStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controller.stop();
                }
            });

            voiceChatStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void closeVoiceChatDialog() {
        if (voiceChatStage != null) {
            voiceChatStage.close();
        }
    }
}
