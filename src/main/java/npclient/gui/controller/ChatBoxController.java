package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import npclient.MyAccount;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.callback.SubscribedTopicListener;
import npclient.core.command.Publisher;
import npclient.core.command.Subscriber;
import npclient.core.data.MessageManager;
import npclient.core.data.Messages;
import npclient.core.entity.Message;
import npclient.gui.view.SendMessageCell;
import nputils.DataTransfer;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {

    @FXML
    private Label lTitle;
    @FXML
    private ListView<Message> lvMessage;
    @FXML
    private TextField tfInput;

    private String target;

    private Subscriber messageSubscriber;

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
        send(tfInput.getText());
    }

    @FXML
    public void attachFile() {

    }

    @FXML void startVoiceCall() {

    }

    private void send(String input) {
        final String topic = String.format("chat/%s", target);
        final String username = MyAccount.getInstance().getName();

        new Publisher(topic, username)
                .putData(input)
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        Message m = new Message();
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

        disconnectSubscriber();
        generateSubscriber(target);
        messageSubscriber.listen();
    }

    public final String getTitle() {
        return target;
    }

    private void disconnectSubscriber() {
        if (messageSubscriber != null) {
            messageSubscriber.cancel();
        }
    }

    private void generateSubscriber(final String target) {
        final String topic = String.format("chat/%s", target);
        final String username = MyAccount.getInstance().getName();
        messageSubscriber = new Subscriber(topic, username)
                .setNewMessageListener(new SubscribedTopicListener() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        Object content = message.data;
                        if (content instanceof String) {
                            Message m = new Message();
                            m.setFrom(message.name);
                            m.setTime(message.datetime);
                            m.setContent(content.toString());
                            Messages messages = MessageManager.getInstance().append(target, m);
                            lvMessage.getItems().setAll(messages);
                        }
                    }
                });
    }
}
