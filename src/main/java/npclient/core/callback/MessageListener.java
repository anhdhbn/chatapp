package npclient.core.callback;

import nputils.DataTransfer;

/**
 * On Receive Message Callback
 */
public interface MessageListener extends ICallback {
    /**
     * Fire when receive new data
     * @param message data transfer wrap info of a packet
     */
    void onReceive(DataTransfer message);
}
