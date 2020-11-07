package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import npclient.core.entity.Message;
import npclient.gui.view.SendMessageCell;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatBoxController implements Initializable {

    @FXML
    private Label lTitle;
    @FXML
    private ListView<Message> lvMessage;
    @FXML
    private TextField tfInput;

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
        Message message = new Message();
        message.setContent(input);
        lvMessage.getItems().add(message);
        lvMessage.refresh();
        clearInput();
    }

    private void clearInput() {
        tfInput.clear();
    }

    public void setTitle(String title) {
        lTitle.setText(title);
    }
}
