package npclient.gui.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import npclient.MyAccount;
import npclient.core.callback.OnAcceptListener;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.callback.OnRejectListener;
import npclient.core.callback.SubscribedTopicListener;
import npclient.core.command.Publisher;
import npclient.core.command.Subscriber;
import npclient.exception.InvalidNameException;
import npclient.gui.util.AudioUtils;
import npclient.gui.view.*;
import nputils.Emoji;
import nputils.FileInfo;
import npclient.exception.DuplicateGroupException;
import npclient.gui.entity.*;
import npclient.gui.manager.MessageManager;
import npclient.gui.manager.MessageSubscribeManager;
import npclient.gui.util.UIUtils;
import nputils.Constants;
import nputils.DataTransfer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BaseController implements Initializable {

    @FXML
    private TextField tfGroup;
    @FXML
    private ListView<ChatItem> lvGroupItem;
    @FXML
    private Text tUsername;
    @FXML
    private CircleImageView civAvatar;
    @FXML
    private AnchorPane paneChatBox;
    @FXML
    private ListView<ChatItem> lvUserItem;

    private VoiceChatDialog voiceChatStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Callback<ListView<ChatItem>, ListCell<ChatItem>> cellFactory = new Callback<ListView<ChatItem>, ListCell<ChatItem>>() {
            @Override
            public ListCell<ChatItem> call(ListView<ChatItem> param) {
                return new ChatItemCell();
            }
        };

        lvUserItem.setCellFactory(cellFactory);
        lvGroupItem.setCellFactory(cellFactory);

        ChangeListener<ChatItem> itemChangeListener = new ChangeListener<ChatItem>() {
            @Override
            public void changed(ObservableValue<? extends ChatItem> observable, ChatItem oldChat, ChatItem newChat) {
                if (newChat != null)
                    changeChatBox(newChat.getName(), newChat instanceof GroupChatItem);
            }
        };

        lvUserItem.getSelectionModel().selectedItemProperty().addListener(itemChangeListener);
        lvGroupItem.getSelectionModel().selectedItemProperty().addListener(itemChangeListener);

        listenOnlineUsers();

        listenVoiceCall();

        final String name = MyAccount.getInstance().getName();
        this.tUsername.setText(name);
        this.civAvatar.update(name);

        this.civAvatar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                EmojiChooser chooser = new EmojiChooser();
                chooser.show();
            }
        });
    }

    /**
     * Listen to voice call signal
     */
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
     * Listen to online users
     */
    private void listenOnlineUsers() {
        final String username = MyAccount.getInstance().getName();
        new Subscriber(Constants.ONLINE_TOPIC, username)
                .setNewMessageListener(new SubscribedTopicListener() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        // Get current user in chat box
                        String current = null;

                        ChatBox currentChatBox = getCurrentChat();
                        if (currentChatBox != null)
                            current = currentChatBox.getTarget();

                        boolean isCurrentOnline = current == null;

                        // Clear all exist chat item
                        lvUserItem.getItems().clear();

                        // Retrieve online users
                        List<String> onlineUsers = (List<String>) message.data;

                        // Clear all offline user chat messages in MessageManager
                        MessageManager.getInstance().clearOffline(onlineUsers);
                        MessageSubscribeManager.getInstance().clearOffline(onlineUsers);

                        for (String user : onlineUsers) {
                            // Add user (not your self) into listview
                            if (!username.equals(user)) {
                                ChatItem item = new UserChatItem();
                                item.setName(user);
                                lvUserItem.getItems().add(item);

                                // Check whether current user still online
                                if (user.equals(current))
                                    isCurrentOnline = true;
                                else {
                                    String topic = String.format("chat/%s", user);
                                    if (!MessageSubscribeManager.getInstance().containsKey(topic)) {
                                        // with other user listen message
                                        Subscriber subscriber = subscribeMessages(username, topic);
                                        MessageSubscribeManager.getInstance().put(topic, subscriber);
                                    }
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

    /**
     * Generate subscriber subscribe listen to message from a user
     * @param username of current user
     * @param topic topic
     * @return subscriber
     */
    private Subscriber subscribeMessages(String username, String topic) {
        Subscriber subscriber = new Subscriber(topic, username)
                .setNewMessageListener(new SubscribedTopicListener() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        onReceiveNewMessage(topic, message);
                    }
                });
        subscriber.listen();
        return subscriber;
    }

    /**
     * Callback fires when receive new message from user
     * @param topic subscribed topic
     * @param message received
     */
    private void onReceiveNewMessage(String topic, DataTransfer message) {
        Message msg = null;

        Object content = message.data;
        if (content instanceof String) {
            TextMessage textMessage = new TextMessage();
            textMessage.setContent(content.toString());
            msg = textMessage;
        } else if (content instanceof FileInfo) {
            FileMessage fileMessage = new FileMessage();
            fileMessage.setContent((FileInfo) content);
            msg = fileMessage;
        } else if (content instanceof Emoji) {
            EmojiMessage emojiMessage = new EmojiMessage();
            emojiMessage.setContent((Emoji) content);
            msg = emojiMessage;
        }

        if (msg != null) {
            msg.setFrom(message.name);
            msg.setTime(message.datetime);

            Messages messages = MessageManager.getInstance().append(topic, msg);
            boolean isGroup = messages.isGroup();

            boolean isCurrentChat = false;
            ChatBox chatBox = getCurrentChat();
            if (chatBox != null) {
                String current = chatBox.getTarget();
                if ((!isGroup && message.name.equals(current))
                        || (isGroup && topic.equals(String.format("group/%s", current)))) {
                    isCurrentChat = true;
                }
            }

            if (isCurrentChat) {
                chatBox.setItems(messages);
                messages.setSeen(true);
            } else {
                messages.setSeen(false);
            }

            updateChatItems(messages);
        }
    }

    /**
     * Get current chatting user
     * @return current chatting username
     */
    private ChatBox getCurrentChat() {
        if (!paneChatBox.getChildren().isEmpty()) {
            Node first = paneChatBox.getChildren().get(0);
            if (first instanceof ChatBox) {
                return (ChatBox) first;
            }
        }

        return null;
    }

    /**
     * Clear chat box section
     */
    private void clearChatBox() {
        paneChatBox.getChildren().clear();
    }

    /**
     * Change chat box section by username
     * @param target name
     * @param isGroup is group chat
     */
    private void changeChatBox(String target, boolean isGroup) {
        ChatBox prevChatBox = getCurrentChat();
        // if reselect a target, do nothing
        if (prevChatBox != null && prevChatBox.getTarget().equals(target))
            return;

        ChatBox chatBox = new ChatBox();
        chatBox.setOnSendListener(new ChatBoxController.OnSendListener() {
            @Override
            public void onSend(Messages messages) {
                updateChatItems(messages);
            }
        });
        chatBox.setTarget(target, isGroup);

        clearChatBox();
        paneChatBox.getChildren().add(chatBox);
    }

    private void onReceiveVoiceQuit(DataTransfer message) {
        MyAccount.getInstance().setInCall(false);
        closeVoiceChatDialog();
        UIUtils.showSimpleAlert(Alert.AlertType.INFORMATION, "Called end.");
    }

    private void onReceiveVoiceReject(DataTransfer message) {
        MyAccount.getInstance().setInCall(false);
        String content = String.format("%s rejected your call request.", message.name);
        UIUtils.showSimpleAlert(Alert.AlertType.INFORMATION, content);
    }

    private void onReceiveVoiceAccept(DataTransfer message) {
        openVoiceChatDialog(message.name);
    }

    private void onReceiveVoiceRequest(DataTransfer message) {
        boolean inCall = MyAccount.getInstance().isInCall();
        final String username = MyAccount.getInstance().getName();
        final String resTopic = String.format("voice/%s", message.name);

        if (inCall || !AudioUtils.isVoiceChatSupported()) {
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
                                public void onReceive(DataTransfer m) {
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
            voiceChatStage = new VoiceChatDialog();
            voiceChatStage.setUsername(MyAccount.getInstance().getName());
            voiceChatStage.setTarget(target);
            voiceChatStage.show();
        } catch (LineUnavailableException | IOException e) {
            // Quit if catch a exception
            final String topic = "voice/" + target;
            final String username = MyAccount.getInstance().getName();
            new Publisher(topic, username)
                    .putData(Constants.VOICE_QUIT)
                    .post();
            UIUtils.showErrorAlert("System not support voice chat: " + e.getMessage());
        }
    }

    private void closeVoiceChatDialog() {
        if (voiceChatStage != null) {
            voiceChatStage.close();
        }
    }

    private synchronized void updateChatItems(Messages messages) {
        ListView<ChatItem> listView = messages.isGroup() ? lvGroupItem : lvUserItem;
        String name = messages.getTopic().split(Constants.SPLITTER)[1];

        ChatItem chatItem = listView.getItems().stream()
                .filter(i -> i.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (chatItem != null) {
            // update chat item info
            chatItem.update(messages);

            // swap to first
            listView.getItems().remove(chatItem);
            listView.getItems().add(0, chatItem);
            listView.getSelectionModel().select(0);
        }
    }

    public synchronized void join(String group) throws DuplicateGroupException, InvalidNameException {
        if (UIUtils.isInvalid(group))
            throw new InvalidNameException(group);

        String topic = String.format("group/%s", group);
        if (!MessageSubscribeManager.getInstance().containsKey(topic)) {
            ChatItem item = new GroupChatItem();
            item.setName(group);
            item.setSeen(true);

            if (!lvGroupItem.getItems().contains(item)) {
                lvGroupItem.getItems().add(item);
            }

            // with other user listen message
            final String username = MyAccount.getInstance().getName();
            Subscriber subscriber = subscribeMessages(username, topic);
            MessageSubscribeManager.getInstance().put(topic, subscriber);

        } else
            throw new DuplicateGroupException(group);
    }

    public void joinGroup(ActionEvent actionEvent) {
        String group = tfGroup.getText().trim();
        try {
            join(group);
        } catch (DuplicateGroupException | InvalidNameException e) {
            UIUtils.showErrorAlert(e.getMessage());
        } finally {
            tfGroup.clear();
        }
    }
}
