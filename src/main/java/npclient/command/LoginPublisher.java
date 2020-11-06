package npclient.command;

import javafx.application.Platform;
import npclient.core.Connection;
import npclient.core.callback.OnLoginSuccess;
import nputils.Constants;
import nputils.DataTransfer;

import java.io.IOException;

public class LoginPublisher extends AbstractPublisher {

    private OnLoginSuccess onLoginSuccess;

    public LoginPublisher(String username) {
        super(null, username);
        dataTransfer = new DataTransfer(topic, username, Constants.INIT_COMMAND);
    }

    public LoginPublisher setOnLoginSuccess(OnLoginSuccess onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        return this;
    }

    @Override
    protected void postProcess(Connection conn) throws IOException {
        if (onLoginSuccess != null) {
            Platform.runLater(() -> onLoginSuccess.onLogin(username, conn));
        }
    }
}
