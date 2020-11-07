package npclient.core.command;

import npclient.core.TCPConnection;
import npclient.core.callback.ErrorListener;

import java.io.IOException;

public abstract class AbstractPublisher extends AbstractTask {

    protected final String username;

    public AbstractPublisher(String username) {
        this.username = username;
    }

    public abstract void post();
}
