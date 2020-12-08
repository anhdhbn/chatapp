package npclient.gui.controller;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import npclient.gui.task.AddMessageTask;
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
import java.util.concurrent.ExecutionException;

public class ChatBoxController implements Initializable {

    @FXML
    private Button btnVoiceCall;
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
        clearInput();
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
        AddMessageTask addMessageTask = generateAddMessageTask(emoji);
        addMessageTask.start();
    }

    private void sendFile(File file) {
        AddMessageTask addMessageTask = generateAddMessageTask(file);

        addMessageTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                UIUtils.showErrorAlert("Can't attach chosen file: " + addMessageTask.getException().getMessage());
            }
        });

        addMessageTask.start();
    }

    private void sendText(String input) {
        AddMessageTask addMessageTask = generateAddMessageTask(input);
        addMessageTask.start();
    }

    private AddMessageTask generateAddMessageTask(Object content) {
        final String topic = getMessageTopic();

        AddMessageTask addMessageTask = new AddMessageTask(topic);
        addMessageTask.setContent(content);

        addMessageTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Messages messages = addMessageTask.getValue();
                if (listener != null)
                    listener.onSend(messages);
                addItem(messages.newest());
            }
        });

        addMessageTask.setStateChangeListener(new AddMessageTask.OnStateChangeListener() {
            @Override
            public void onChange(Message message, Message.State state) {
                lvMessage.refresh();
            }
        });

        return addMessageTask;
    }

    private void clearInput() {
        tfInput.clear();
    }

    public void setTitle(String target, boolean isGroup) {
        this.isGroup = isGroup;
        this.target = target;
        String title;
        if (isGroup) {
            title = String.format("g:/%s", target);
            voiceCallIcon.setOpacity(0.5f);
            btnVoiceCall.setDisable(true);
        } else {
            title = String.format("u:/%s", target);
            voiceCallIcon.setOpacity(1f);
            btnVoiceCall.setDisable(false);
        }
        this.lTitle.setText(title);

        // load exist message
        String topic = getMessageTopic();
        Messages existMessages = MessageManager.getInstance().get(topic);
        if (existMessages != null)
            setItems(existMessages);
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

    public void setItems(Messages messages) {
        lvMessage.getItems().setAll(messages);
    }

    public void addItem(Message message) {
        lvMessage.getItems().add(message);
    }

    public void setOnSendListener(OnSendListener listener) {
        this.listener = listener;
    }

    public interface OnSendListener {
        void onSend(Messages messages);
    }
}
