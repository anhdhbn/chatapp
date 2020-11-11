package npclient.core.command;

import javafx.application.Platform;
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

    protected void handleError(Exception e) {
        if (errorListener != null) {
            try {
                Platform.runLater(() -> errorListener.onReceive(e));
            } catch (IllegalStateException ex) {
                errorListener.onReceive(e);
            }
        } else {
            e.printStackTrace();
        }
    }
}
