package npclient.gui.task;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import npclient.gui.util.UIUtils;
import nputils.Emoji;

import java.io.IOException;
import java.io.InputStream;

public class RetrieveEmojiTask extends Task<Image> {

    private final Emoji emoji;

    public RetrieveEmojiTask(Emoji emoji) {
        this.emoji = emoji;
    }

    public RetrieveEmojiTask setView(ImageView view) {
        setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Image image = getValue();
                view.setImage(image);
            }
        });
        return this;
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    protected Image call() {
        return UIUtils.Emoji.toImage(emoji);
    }
}
