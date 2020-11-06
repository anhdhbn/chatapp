package npclient.command;

import javafx.application.Platform;
import npclient.core.Connection;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.SubscribedTopicListener;
import npclient.core.logger.CliLogger;
import nputils.Constants;
import nputils.DataTransfer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Subscriber implements Runnable {

    private static final CliLogger logger = CliLogger.get(Subscriber.class);

    private final String topic;

    private boolean isCancel = false;

    private SubscribedTopicListener newMsgListener;

    private ErrorListener errListener;

    private final String username;

    public Subscriber(String topic, String username) {
        this.topic = topic;
        this.username = username;
    }

    public Subscriber setNewMessageListener(SubscribedTopicListener listener) {
        this.newMsgListener = listener;
        return this;
    }

    public Subscriber setOnErrorListener(ErrorListener listener) {
        this.errListener = listener;
        return this;
    }

    public Subscriber stop() {
        isCancel = true;
        return this;
    }

    public void listen() {
        String threadName = String.format("%s Subscriber Thread", topic);
        new Thread(this, threadName).start();
    }

    public void run() {
        try {
            logger.debug("Initialize a subscribe connection");
            Connection subConn = new Connection();

            logger.debug("Send subscribe signal");
            ObjectOutputStream outputStream = new ObjectOutputStream(subConn.getOutputStream());
            DataTransfer subSignal = new DataTransfer(topic, username, Constants.SUBSCRIBE);
            outputStream.writeObject(subSignal);
            Thread.sleep(12000);

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
        if (errListener != null) {
            logger.debug("On Error Callback");
            Platform.runLater(() -> errListener.onReceive(e));
        } else {
            e.printStackTrace();
        }
    }
}
