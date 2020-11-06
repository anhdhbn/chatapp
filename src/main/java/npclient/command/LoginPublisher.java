package npclient.command;

import npclient.core.Connection;
import npclient.core.callback.OnLoginSuccess;
import nputils.Constants;
import transferable.DataTransfer;

import java.io.IOException;

public class LoginPublisher extends AbstractPublisher {

    private OnLoginSuccess onLoginSuccess;

    public LoginPublisher(String username) {
        super(null, username);
        dataTransfer = new DataTransfer(topic, username, Constants.INIT_COMMAND);
    }

    public void setOnLoginSuccess(OnLoginSuccess onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    @Override
    protected void postProcess(Connection conn) throws IOException {
        if (onLoginSuccess != null) {
            onLoginSuccess.onLogin(username, conn);
        }
    }
}
