package npclient.core.callback;

import npclient.core.TCPConnection;

public interface OnLoginSuccess {
    void onLogin(String username, TCPConnection mainConnection);
}
