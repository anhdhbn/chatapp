package npclient.core.command;

import javafx.application.Platform;
import npclient.core.TCPConnection;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.CliLogger;
import nputils.Constants;
import nputils.DataTransfer;

import java.io.ObjectInputStream;
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

            ObjectOutputStream outputStream = new ObjectOutputStream(publishConn.getOutputStream());
            DataTransfer initData = new DataTransfer(Constants.INITIALIZE_TOPIC, username, Constants.INIT_COMMAND);
            outputStream.writeObject(initData);

            if (!isCancel) {
                ObjectInputStream inputStream = new ObjectInputStream(publishConn.getInputStream());
                logger.debug("Publish " + dataTransfer + " to topic " + topic);
                handlePublish(outputStream, inputStream);
            }

            logger.debug("Close publish connection");
            publishConn.close();

        } catch (Exception e) {
            logger.error("Failed to publish: " + e.getMessage());
            handleError(e);
        }
    }

    protected void handlePublish(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws Exception {
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
}
