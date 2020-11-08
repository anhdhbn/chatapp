package npclient.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import npclient.MyAccount;
import npclient.core.UDPConnection;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.command.LoginPublisher;
import npclient.gui.manager.StageManager;
import npclient.gui.util.UIUtils;
import nputils.DataTransfer;

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
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        UDPConnection udpConn = (UDPConnection) message.data;
                        loginSuccess(message.name, udpConn);
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

    private void loginSuccess(String name, UDPConnection udpConn) {
        MyAccount.register(name, udpConn);
        Parent baseScene = UIUtils.load("/fxml/base.fxml");
        StageManager.getInstance().changeScene(baseScene);
    }

    private void loginFailure() {
        UIUtils.showSimpleAlert(Alert.AlertType.ERROR, "Failed to login! Please check your connection");
    }
}
