package npclient.command;

import npclient.core.Connection;
import npclient.core.callback.OnLoginSuccess;
import nputils.Constants;
import nputils.DataTransfer;

public class LoginPublisher extends AbstractPublisher {

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
    protected void postProcess(Connection conn) {
        if (onLoginSuccess != null) {
            /*Platform.runLater(() -> */onLoginSuccess.onLogin(username, conn)/*)*/;
        }
    }
}
