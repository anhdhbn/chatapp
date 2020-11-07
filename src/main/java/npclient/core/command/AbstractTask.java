package npclient.core.command;

import npclient.core.callback.ErrorListener;

public abstract class AbstractTask implements Runnable {

    protected boolean isCancel = false;

    protected ErrorListener errorListener;

    public void cancel() {
        isCancel = true;
    }

    public AbstractTask setErrorListener(ErrorListener listener) {
        this.errorListener = listener;
        return this;
    }
}
