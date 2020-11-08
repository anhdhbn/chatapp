package npclient.core.command;

import javafx.application.Platform;
import npclient.core.TCPConnection;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.CliLogger;
import nputils.Constants;
import nputils.DataTransfer;

import java.io.IOException;
import java.io.ObjectOutputStream;

public abstract class TCPPublisher extends AbstractPublisher {

    protected static final CliLogger logger = CliLogger.get(TCPPublisher.class);

    protected DataTransfer dataTransfer;

    protected final String topic;

    protected OnPublishMessageSuccess successListener;

    public TCPPublisher(String topic, String username) {
        super(username);
        this.topic = topic;
    }

    public TCPPublisher setErrorListener(ErrorListener listener) {
        this.errorListener = listener;
        return this;
    }

    public TCPPublisher setSuccessListener(OnPublishMessageSuccess listener) {
        this.successListener = listener;
        return this;
    }

    public void post() {
        String threadName = topic + " Publisher Thread";
        new Thread(this, threadName).start();
    }

    @Override
    public void run() {
        try {
            logger.debug("Initialize a publish connection");
            TCPConnection publishConn = new TCPConnection();

            logger.debug("Publish " + dataTransfer + " to topic " + topic);
            ObjectOutputStream outputStream = new ObjectOutputStream(publishConn.getOutputStream());
            DataTransfer initData = new DataTransfer(Constants.INITIALIZE, username, Constants.INIT_COMMAND);
            outputStream.writeObject(initData);

            if (!isCancel) {
                outputStream.writeObject(dataTransfer);

                if (successListener != null) {
                    logger.debug("On Success Callback");
                    try {
                        Platform.runLater(() -> successListener.onReceive(dataTransfer));
                    } catch (IllegalStateException ex) {
                        successListener.onReceive(dataTransfer);
                    }
                }
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

    protected abstract void postProcess(TCPConnection conn) throws IOException;
}
