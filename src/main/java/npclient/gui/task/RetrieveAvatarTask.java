package npclient.gui.task;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import npclient.gui.util.UIUtils;

import java.io.IOException;
import java.io.InputStream;

public class RetrieveAvatarTask extends Task<Image> {

    private final String name;

    public RetrieveAvatarTask(String name) {
        this.name = name;
    }

    @Override
    protected Image call() throws IOException {
        InputStream stream = UIUtils.retrieveAvatar(name);
        return new Image(stream);
    }
}
