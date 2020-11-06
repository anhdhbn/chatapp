package npclient.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import npclient.command.LoginPublisher;
import npclient.core.Connection;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.OnLoginSuccess;
import npclient.gui.StageManager;
import npclient.gui.utils.FXMLUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public TextField tfUsername;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void onEnter(ActionEvent actionEvent) {
        String username = tfUsername.getText();
        login(username);
    }

    private void login(String username) {
        new LoginPublisher(username)
                .setOnLoginSuccess(new OnLoginSuccess() {
                    @Override
                    public void onLogin(String username, Connection connection) {
                        loginSuccess();
                    }
                })
                .setErrorListener(new ErrorListener() {
                    @Override
                    public void onReceive(Exception err) {
                        loginFailure();
                    }
                })
                .post();
    }

    private void loginSuccess() {
        FXMLUtils.showSimpleAlert(Alert.AlertType.CONFIRMATION, "Login Successful!");
        Parent baseScene = FXMLUtils.load("/fxml/base.fxml");
        StageManager.getInstance().changeScene(baseScene);
    }

    private void loginFailure() {
        FXMLUtils.showSimpleAlert(Alert.AlertType.ERROR, "Failed to login! Please check your connection");
    }
}
