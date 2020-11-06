package npclient.core.callback;

import npclient.core.Connection;

public interface OnLoginSuccess {
    void onLogin(String username, Connection connection);
}
