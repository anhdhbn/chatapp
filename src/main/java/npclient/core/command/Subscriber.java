package npclient.core.command;

import javafx.application.Platform;
import npclient.core.TCPConnection;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.SubscribedTopicListener;
import npclient.CliLogger;
import nputils.Constants;
import nputils.DataTransfer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Subscriber extends AbstractTask {

    private static final CliLogger logger = CliLogger.get(Subscriber.class);

    private final String topic;

    private SubscribedTopicListener newMsgListener;

    private final String username;

    public Subscriber(String topic, String username) {
        this.topic = topic;
        this.username = username;
    }

    public Subscriber setNewMessageListener(SubscribedTopicListener listener) {
        this.newMsgListener = listener;
        return this;
    }

    @Override
    public Subscriber setErrorListener(ErrorListener listener) {
        return (Subscriber) super.setErrorListener(listener);
    }

    public void listen() {
        String threadName = String.format("%s Subscriber Thread", topic);
        new Thread(this, threadName).start();
    }

    public void run() {
        try {
            logger.debug("Initialize a subscribe connection");
            TCPConnection subConn = new TCPConnection();

            ObjectOutputStream outputStream = new ObjectOutputStream(subConn.getOutputStream());

            DataTransfer initData = new DataTransfer(Constants.INITIALIZE, username, Constants.INIT_COMMAND);
            outputStream.writeObject(initData);

            logger.debug("Send subscribe signal");
            DataTransfer subSignal = new DataTransfer(topic, username, Constants.SUBSCRIBE);
            outputStream.writeObject(subSignal);
            Thread.sleep(1000);

            logger.debug("Listening data from server");
            ObjectInputStream inputStream = new ObjectInputStream(subConn.getInputStream());

            while (!isCancel) {
                try {
                    DataTransfer data = (DataTransfer) inputStream.readObject();
                    if (newMsgListener != null && data != null) {
                        logger.debug("On New Message Callback");
                        Platform.runLater(() -> newMsgListener.onReceive(data));
                    }

                } catch (Exception e) {
                    handleError(e);
                }
            }

            logger.debug("Close subscribe connection");
            subConn.close();

        } catch (IOException | InterruptedException e) {
            handleError(e);
        }
    }

    private void handleError(Exception e) {
        logger.error("Failed to subscribe: " + e.getMessage());
        if (errorListener != null) {
            logger.debug("On Error Callback");
            Platform.runLater(() -> errorListener.onReceive(e));
        } else {
            e.printStackTrace();
        }
    }
}
