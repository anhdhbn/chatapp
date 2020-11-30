package npclient.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        StageManager.getInstance().setPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
