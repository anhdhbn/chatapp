package npclient.gui.view;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import npclient.gui.controller.VoiceChatController;
import npclient.gui.manager.StageManager;

import java.io.IOException;

public class VoiceChatDialog extends Stage {

    private VoiceChatController controller;

    public VoiceChatDialog() {
        setTitle("Voice Chat");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/voice_chat.fxml"));
            AnchorPane view = new AnchorPane();
            fxmlLoader.setRoot(view);
            fxmlLoader.load();

            controller = fxmlLoader.getController();

            Scene scene = new Scene(view);
            setScene(scene);

            initOwner(StageManager.getInstance().getPrimaryStage());

            setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controller.stop();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        controller.setUser1(username);
    }

    public void setTarget(String target) {
        controller.setUser2(target);
    }
}
