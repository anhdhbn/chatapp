package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import npclient.gui.util.UIUtils;
import nputils.Emoji;

import java.net.URL;
import java.util.ResourceBundle;

public class EmojiMessageController implements Initializable {

    @FXML
    private ImageView ivEmoji;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setEmoji(Emoji emoji) {
        Image image = UIUtils.Emoji.toImage(emoji);
        this.ivEmoji.setImage(image);
    }
}
