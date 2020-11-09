package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import npclient.MyAccount;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.command.Publisher;
import npclient.core.transferable.FileInfo;
import npclient.gui.entity.FileMessage;
import npclient.gui.manager.MessageManager;
import npclient.gui.entity.Messages;
import npclient.gui.entity.Message;
import npclient.gui.entity.TextMessage;
import npclient.gui.manager.StageManager;
import npclient.gui.util.UIUtils;
import npclient.gui.view.SendMessageCell;
import nputils.Constants;
import nputils.DataTransfer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {

    @FXML
    private Label lTitle;
    @FXML
    private ListView<Message> lvMessage;
    @FXML
    private TextField tfInput;

    private String target;

//    private Subscriber messageSubscriber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lvMessage.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new SendMessageCell();
            }
        });
    }

    @FXML
    public void onEnter() {
        sendText(tfInput.getText());
    }

    @FXML
    public void attachFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose File");
        Stage primaryStage = StageManager.getInstance().getPrimaryStage();
        File file = chooser.showOpenDialog(primaryStage);
        sendFile(file);
    }

    @FXML void startVoiceCall() {
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

    private void sendFile(File file) {
        final String topic = String.format("chat/%s", target);
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
                            m.setFileInfo(fileInfo);
                            m.setTime(System.currentTimeMillis());
                            Messages messages = MessageManager.getInstance().append(target, m);
                            lvMessage.getItems().setAll(messages);
                        }
                    }).post();
        } catch (NoSuchAlgorithmException | IOException e) {
            UIUtils.showErrorAlert("Can't attach chosen file: " + e.getMessage());
        }


    }

    private void sendText(String input) {
        final String topic = String.format("chat/%s", target);
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
                        Messages messages = MessageManager.getInstance().append(target, m);
                        lvMessage.getItems().setAll(messages);
                        clearInput();
                    }
                }).post();
    }

    private void clearInput() {
        tfInput.clear();
    }

    public void setTitle(String title) {
        target = title;
        lTitle.setText(target);

        // load exist message
        Messages existMessages = MessageManager.getInstance().get(target);
        if (existMessages != null)
            setItem(existMessages);
    }

    public final String getTitle() {
        return target;
    }

    public void setItem(Messages messages) {
        lvMessage.getItems().setAll(messages);
    }
}
