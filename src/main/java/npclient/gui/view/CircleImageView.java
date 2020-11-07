package npclient.gui.view;

import javafx.beans.NamedArg;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class CircleImageView extends Circle {

    public CircleImageView(@NamedArg("radius") float radius, @NamedArg("url") String url) {
        super(radius);
        Image image = new Image(url);
        setFill(new ImagePattern(image));
    }
}
