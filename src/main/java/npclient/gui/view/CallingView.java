package npclient.gui.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import npclient.gui.audio.CallingAudio;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CallingView extends HBox implements Initializable {

    @FXML
    private Text text;

    @FXML
    private ImageView icon;

    private CallingAudio audio;

    public CallingView() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load(getClass().getResourceAsStream("/fxml/calling.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(String name) {
        audio = new CallingAudio();
        audio.start();
        setName(name);
        setVisible(true);
    }

    public void hide() {
        if (audio != null)
            audio.stopMedia();
        setVisible(false);
    }

    private void setName(String name) {
        text.setText(String.format("You are calling %s", name));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        icon.setImage(new Image("/img/calling.png"));
    }
}
