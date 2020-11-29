package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TextMessageController implements Initializable {

    @FXML
    private Text tContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setContent(String content) {
        this.tContent.setText(content);
        if (content.length() > 42) {
            tContent.setWrappingWidth(264);
        }
    }
}
