package npclient.command;

import npclient.core.Connection;
import npclient.core.callback.ErrorListener;
import npclient.core.callback.SubscribedTopicListener;
import npclient.core.logger.CliLogger;
import transferable.DataTransfer;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Subscriber implements Runnable {

    private static final CliLogger logger = CliLogger.get(Subscriber.class);

    private final String topic;

    private boolean isCancel = false;

    private SubscribedTopicListener newMsgListener;

    private ErrorListener errListener;


    public Subscriber(String topic) {
        this.topic = topic;
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

    public Subscriber listen() {
        String threadName = String.format("%s Subscriber Thread", topic);
        new Thread(this, threadName).start();
        return this;
    }

    public void run() {
        try {
            logger.debug("Initialize a subscribe connection");
            Connection subConn = new Connection();

            logger.debug("Listening data from server");
            ObjectInputStream inputStream = new ObjectInputStream(subConn.getInputStream());

            while (!isCancel) {
                try {
                    DataTransfer data = (DataTransfer) inputStream.readObject();
                    if (newMsgListener != null && data != null) {
                        logger.debug("On New Message Callback");
                        newMsgListener.onReceive(data);
                    }

                } catch (Exception e) {
                    handleError(e);
                }
            }

            logger.debug("Close subscribe connection");
            subConn.close();

        } catch (IOException e) {
            handleError(e);
        }
    }

    private void handleError(Exception e) {
        logger.error("Failed to publish: " + e.getMessage());
        if (errListener != null) {
            logger.debug("On Error Callback");
            errListener.onReceive(e);
        } else {
            e.printStackTrace();
        }
    }
}
