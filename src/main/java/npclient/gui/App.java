package npclient.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import npclient.gui.utils.FXMLUtils;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLUtils.load("/fxml/login.fxml");
        Scene scene = new Scene(root);

        primaryStage.setTitle("Chatapp");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        StageManager.getInstance().setPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
