package npclient.gui.view;

import javafx.beans.NamedArg;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import npclient.gui.util.UIUtils;

import java.io.IOException;
import java.io.InputStream;

public class CircleImageView extends Circle {

    public CircleImageView(@NamedArg("radius") float radius) {
        super(radius);
    }

    public void update(String name) {
        Image image;
        try {
            InputStream stream = UIUtils.retrieveAvatar(name);
            image = new Image(stream);
            setFill(new ImagePattern(image));
        } catch (IOException e) {
            image = new Image("/img/avatar-100.jpg");
        }
        setFill(new ImagePattern(image));
    }
}
