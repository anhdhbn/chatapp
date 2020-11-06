package npclient.core.callback;

import transferable.DataTransfer;

public interface MessageListener {
    void onReceive(DataTransfer message);
}
