package npclient.core.command;

import javafx.application.Platform;
import npclient.core.TCPConnection;
import npclient.core.callback.OnLoginSuccess;
import nputils.Constants;
import nputils.DataTransfer;

public class LoginPublisher extends TCPPublisher {

    private OnLoginSuccess onLoginSuccess;

    public LoginPublisher(String username) {
        super(Constants.INITIALIZE, username);
        dataTransfer = new DataTransfer(topic, username, Constants.INIT_COMMAND);
    }

    public LoginPublisher setOnLoginSuccess(OnLoginSuccess onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        return this;
    }

    @Override
    protected void postProcess(TCPConnection conn) {
        if (onLoginSuccess != null) {
            Platform.runLater(() -> onLoginSuccess.onLogin(username, conn));
        }
    }
}
