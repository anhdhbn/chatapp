package npclient.gui.view;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import npclient.gui.controller.VoiceChatController;
import npclient.gui.manager.StageManager;
import npclient.gui.util.AudioUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class VoiceChatDialog extends Stage {

    private final VoiceChatController controller;

    public VoiceChatDialog() throws IOException, LineUnavailableException {
        setTitle("Voice Chat");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/voice_chat.fxml"));
        AnchorPane view = new AnchorPane();
        fxmlLoader.setRoot(view);
        fxmlLoader.load();

        controller = fxmlLoader.getController();
        AudioFormat format = AudioUtils.getAudioFormat();
        controller.setAudioFormat(format);

        Scene scene = new Scene(view);
        setScene(scene);

        initOwner(StageManager.getInstance().getPrimaryStage());

        setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.stop();
            }
        });
    }

    public void setUsername(String username) {
        controller.setUser1(username);
    }

    public void setTarget(String target) {
        controller.setUser2(target);
    }
}
