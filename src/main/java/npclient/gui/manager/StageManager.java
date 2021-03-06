package npclient.gui.manager;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import npclient.gui.controller.BaseController;

public class StageManager {

    private static StageManager instance;

    private Stage primaryStage;

    private BaseController baseController;

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

    public BaseController getBaseController() {
        return baseController;
    }

    public void setBaseController(BaseController baseController) {
        this.baseController = baseController;
    }

    public void changeScene(Parent root) {
        try {
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            // centering stage
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
        } catch (NullPointerException e) {
            System.exit(1);
        }
    }
}
