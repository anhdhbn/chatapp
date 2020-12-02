package npclient.gui.controller;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import npclient.gui.task.RetrieveEmojiTask;
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
        new RetrieveEmojiTask(emoji)
                .setView(ivEmoji)
                .start();
//        RetrieveEmojiTask task = new RetrieveEmojiTask(emoji);
//        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//                Image image = task.getValue();
//                ivEmoji.setImage(image);
//            }
//        });
//        new Thread(task).start();
//        Image image = UIUtils.Emoji.toImage(emoji);
//        this.ivEmoji.setImage(image);
    }
}
