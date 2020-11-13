package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import npclient.MyAccount;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.command.Publisher;
import npclient.exception.BigFileTransferException;
import npclient.gui.entity.*;
import npclient.gui.manager.MessageManager;
import npclient.gui.manager.StageManager;
import npclient.gui.util.UIUtils;
import npclient.gui.view.EmojiChooser;
import npclient.gui.view.MessageCell;
import nputils.Constants;
import nputils.DataTransfer;
import nputils.Emoji;
import nputils.FileInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {

    @FXML
    private Label lTitle;
    @FXML
    private ListView<Message> lvMessage;
    @FXML
    private TextField tfInput;

    @FXML
    private ImageView sendIcon;
    @FXML
    private ImageView attachFileIcon;
    @FXML
    private ImageView voiceCallIcon;
    @FXML
    private ImageView emojiIcon;


    private String target;
    private boolean isGroup;

    private OnSendListener listener;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lvMessage.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new MessageCell();
            }
        });
        try {
            sendIcon.setImage(new Image(getClass().getResourceAsStream("/img/send.png")));
            attachFileIcon.setImage(new Image(getClass().getResourceAsStream("/img/file.png")));
            voiceCallIcon.setImage(new Image(getClass().getResourceAsStream("/img/call.png")));
            emojiIcon.setImage(new Image(getClass().getResourceAsStream("/img/emoji.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEnter() {
        String message = tfInput.getText().trim();
        if (!message.isEmpty())
            sendText(message);
    }

    @FXML
    public void attachFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose File");
        Stage primaryStage = StageManager.getInstance().getPrimaryStage();
        File file = chooser.showOpenDialog(primaryStage);
        if (file != null)
            sendFile(file);
    }

    @FXML
    public void startVoiceCall() {
        final String topic = String.format("voice/%s", target);
        final String username = MyAccount.getInstance().getName();
        new Publisher(topic, username)
                .putData(Constants.VOICE_REQUEST)
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        MyAccount.getInstance().setInCall(true);
                    }
                })
                .post();
    }

    @FXML
    public void onSendEmoji() {
        EmojiChooser chooser = new EmojiChooser()
                .setListener(new EmojiChooser.OnEmojiListener() {
                    @Override
                    public void onSelect(Emoji emoji) {
                        sendEmoji(emoji);
                    }
                });
        chooser.showAndWait();
    }

    private void sendEmoji(Emoji emoji) {
        final String topic = getMessageTopic();
        final String username = MyAccount.getInstance().getName();

        new Publisher(topic, username)
                .putData(emoji)
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        EmojiMessage m = new EmojiMessage();
                        m.setFrom(username);
                        m.setContent(emoji);
                        m.setTime(System.currentTimeMillis());
                        Messages messages = MessageManager.getInstance().append(topic, m);
                        setItem(messages);
                        if (listener != null) {
                            listener.onSend(messages);
                        }
                    }
                }).post();
    }

    private void sendFile(File file) {
        final String topic = getMessageTopic();
        final String username = MyAccount.getInstance().getName();

        try {
            FileInfo fileInfo = new FileInfo(file);
            new Publisher(topic, username)
                    .putData(fileInfo)
                    .setSuccessListener(new OnPublishMessageSuccess() {
                        @Override
                        public void onReceive(DataTransfer message) {
                            FileMessage m = new FileMessage();
                            m.setFrom(username);
                            m.setContent(fileInfo);
                            m.setTime(System.currentTimeMillis());
                            Messages messages = MessageManager.getInstance().append(topic, m);
                            setItem(messages);
                            if (listener != null) {
                                listener.onSend(messages);
                            }
                        }
                    }).post();
        } catch (IOException | BigFileTransferException e) {
            UIUtils.showErrorAlert("Can't attach chosen file: " + e.getMessage());
        }
    }

    private void sendText(String input) {
        final String topic = getMessageTopic();
        final String username = MyAccount.getInstance().getName();

        new Publisher(topic, username)
                .putData(input)
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        TextMessage m = new TextMessage();
                        m.setFrom(username);
                        m.setContent(input);
                        m.setTime(System.currentTimeMillis());
                        Messages messages = MessageManager.getInstance().append(topic, m);
                        setItem(messages);
                        clearInput();
                        if (listener != null) {
                            listener.onSend(messages);
                        }
                    }
                }).post();
    }



    private void clearInput() {
        tfInput.clear();
    }

    public void setTitle(String target, boolean isGroup) {
        this.target = target;
        String title;
        if (isGroup) title = String.format("g:/%s", target);
        else title = String.format("u:/%s", target);
        this.lTitle.setText(title);
        this.isGroup = isGroup;

        // load exist message
        String topic = getMessageTopic();
        Messages existMessages = MessageManager.getInstance().get(topic);
        if (existMessages != null)
            setItem(existMessages);
    }

    private String getMessageTopic() {
        String topic;
        if (isGroup)
            topic = String.format("group/%s", target);
        else
            topic = String.format("chat/%s", target);
        return topic;
    }

    public final String getTitle() {
        return target;
    }

    public void setItem(Messages messages) {
        lvMessage.getItems().setAll(messages);
    }


    public void setOnSendListener(OnSendListener listener) {
        this.listener = listener;
    }

    public interface OnSendListener {
        void onSend(Messages messages);
    }
}
