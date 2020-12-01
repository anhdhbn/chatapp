package npclient.gui.view;

import javafx.beans.NamedArg;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import npclient.gui.task.RetrieveAvatarTask;
import npclient.gui.util.UIUtils;

import java.io.IOException;
import java.io.InputStream;

public class CircleImageView extends Circle {

    public CircleImageView(@NamedArg("radius") float radius) {
        super(radius);
    }

    public void update(String name) {
        Image defaultImage = new Image("/img/avatar-100.png");
        setFill(new ImagePattern(defaultImage));
        RetrieveAvatarTask task = new RetrieveAvatarTask(name);
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Image image = task.getValue();
                setFill(new ImagePattern(image));
            }
        });
        new Thread(task).start();
    }
}
