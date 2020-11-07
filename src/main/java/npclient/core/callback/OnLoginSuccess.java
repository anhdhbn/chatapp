package npclient.core.callback;

import npclient.core.TCPConnection;

/**
 * On Login Success Callback
 */
public interface OnLoginSuccess extends ICallback {
    /**
     * Fire when login success
     * @param username of current user
     * @param mainConnection of login request
     */
    void onLogin(String username, TCPConnection mainConnection);
}
