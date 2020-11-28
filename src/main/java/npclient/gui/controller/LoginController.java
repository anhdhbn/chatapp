package npclient.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import npclient.MyAccount;
import npclient.core.UDPConnection;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.command.LoginPublisher;
import npclient.exception.InvalidNameException;
import npclient.gui.manager.StageManager;
import npclient.gui.util.UIUtils;
import nputils.DataTransfer;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btnEnter;
    @FXML
    private TextField tfUsername;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void onEnter(ActionEvent actionEvent) {
        lock();
        String username = tfUsername.getText().trim();
        login(username);
    }

    private void lock() {
        tfUsername.setDisable(true);
        btnEnter.setDisable(true);
    }

    private void unlock() {
        tfUsername.setDisable(false);
        btnEnter.setDisable(false);
    }

    private void login(String username) {
        new LoginPublisher(username)
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        UDPConnection udpConn = (UDPConnection) message.data;
                        loginSuccess(message.name, udpConn);
                        unlock();
                    }
                })
                .setErrorListener(new ErrorListener() {
                    @Override
                    public void onReceive(Exception err) {
                        loginFailure(err);
                        unlock();
                    }
                })
                .post();
    }

    private void loginSuccess(String name, UDPConnection udpConn) {
        MyAccount.register(name, udpConn);
        Parent baseScene = UIUtils.load("/fxml/base.fxml");
        StageManager.getInstance().changeScene(baseScene);
        StageManager.getInstance().getPrimaryStage().setMinHeight(864f);
        StageManager.getInstance().getPrimaryStage().setMinWidth(1140f);
    }

    private void loginFailure(Exception err) {
        String content;
        if (err instanceof InvalidNameException)
            content = err.getMessage();
        else
            content = "Failed to login! Please check your connection";

        UIUtils.showSimpleAlert(Alert.AlertType.ERROR, content);
    }
}
