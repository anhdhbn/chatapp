package npclient.core.callback;

import nputils.DataTransfer;

public interface MessageListener {
    void onReceive(DataTransfer message);
}
