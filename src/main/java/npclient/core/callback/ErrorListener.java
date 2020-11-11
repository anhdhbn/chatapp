package npclient.core.callback;

/**
 * On Error Callback
 */
public interface ErrorListener extends ICallback {
    /**
     * Fire when receive a error
     * @param err throwable error
     */
    void onReceive(Exception err);
}
