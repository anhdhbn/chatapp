package npclient.gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import npclient.gui.manager.StageManager;
import npclient.gui.util.UIUtils;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = UIUtils.load("/fxml/login.fxml");

        assert root != null;
        Scene scene = new Scene(root);

        primaryStage.setTitle("Chatapp");
        primaryStage.setScene(scene);
//        primaryStage.setFullScreen(true);
        primaryStage.show();

        StageManager.getInstance().setPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
