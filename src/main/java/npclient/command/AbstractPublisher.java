package npclient.command;

import javafx.application.Platform;
import npclient.core.Connection;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.logger.CliLogger;
import nputils.DataTransfer;

import java.io.IOException;
import java.io.ObjectOutputStream;

public abstract class AbstractPublisher implements Runnable {

    protected static final CliLogger logger = CliLogger.get(AbstractPublisher.class);

    protected DataTransfer dataTransfer;

    protected final String topic;

    protected final String username;

    protected ErrorListener errorListener;

    protected OnPublishMessageSuccess successListener;

    public AbstractPublisher(String topic, String username) {
        this.topic = topic;
        this.username = username;
    }

    public void post() {
        String threadName = topic + " Publisher Thread";
        new Thread(this, threadName).start();
    }

    public AbstractPublisher setErrorListener(ErrorListener listener) {
        this.errorListener = listener;
        return this;
    }

    public AbstractPublisher setSuccessListener(OnPublishMessageSuccess listener) {
        this.successListener = listener;
        return this;
    }

    @Override
    public void run() {
        try {
            logger.debug("Initialize a publish connection");
            Connection publishConn = new Connection();

            logger.debug("Publish " + dataTransfer + " to topic " + topic);
            ObjectOutputStream outputStream = new ObjectOutputStream(publishConn.getOutputStream());
            outputStream.writeObject(dataTransfer);

            if (successListener != null) {
                logger.debug("On Success Callback");
                Platform.runLater(() -> successListener.onReceive(dataTransfer));
            }

            postProcess(publishConn);

        } catch (IOException e) {
            logger.error("Failed to publish: " + e.getMessage());
            if (errorListener != null) {
                logger.debug("On Error Callback");
                Platform.runLater(() -> errorListener.onReceive(e));
            } else {
                e.printStackTrace();
            }
        }
    }

    protected abstract void postProcess(Connection conn) throws IOException;
}
