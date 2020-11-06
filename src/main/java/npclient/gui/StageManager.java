package npclient.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageManager {

    private static StageManager instance;

    private Stage primaryStage;

    public static StageManager getInstance() {
        if (instance == null)
            instance = new StageManager();

        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void changeScene(Parent scene) {
        primaryStage.getScene().setRoot(scene);
    }
}
