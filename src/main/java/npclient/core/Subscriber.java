package npclient.core;

import npclient.core.callback.ErrorListener;
import npclient.core.callback.ISubscribeCallback;
import npclient.core.callback.SubscribedTopicListener;
import npclient.core.callback.UnsubscribedTopicListener;
import npclient.core.logger.CliLogger;
import npclient.exception.ConnectionClosedException;
import npserver.handler.DataTransfer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Subscriber implements Runnable {

    private static final CliLogger logger = CliLogger.get(Subscriber.class);

    private final Map<String, ISubscribeCallback> topicMap = new HashMap<>();

    private boolean isCancel = false;

    private ErrorListener errListener;

    private final Connection connection;

    public Subscriber(Connection connection) {
        this.connection = connection;
    }

    public synchronized Subscriber subscribe(String topic, SubscribedTopicListener listener) {
        topicMap.put(topic, listener);
        return this;
    }

    public synchronized Subscriber unsubscribe(String topic) {
        topicMap.remove(topic);
        return this;
    }

    public void listen() {
        new Thread(this, getClass().getSimpleName()).start();
        logger.debug("Start listening data");
    }

    public void run() {
        while (!isCancel) {
            try {
                if (connection.isClosed()) {
                    isCancel = true;
                }

                logger.debug("Listening data from server...");
                DataTransfer data = read();

                if (data != null) {
                    String topic = data.topic;
                    String message = data.data.toString();

                    synchronized (topicMap) {
                        ISubscribeCallback callback = topicMap.get(topic);
                        if (callback == null) {
                            callback = new UnsubscribedTopicListener();
                            topicMap.put(topic, callback);
                        }

                        if (callback instanceof SubscribedTopicListener) {
                            logger.debug("Receive data from topic " + topic);
                            SubscribedTopicListener msgListener = (SubscribedTopicListener) callback;
                            logger.debug("On Message Callback");
                            msgListener.onReceive(message);
                        }
                    }
                }

            } catch (Exception e) {
                if (e instanceof ConnectionClosedException) {
                    isCancel = true;
                }

                if (errListener != null) {
                    logger.debug("On Error Callback");
                    errListener.onReceive(e);
                } else {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }
        }
    }

    private DataTransfer read() throws IOException {
        return null;
    }

//    private DataWrapper read() throws IOException {
//        DataWrapper data = null;
//
//        byte[] buffer = new byte[1024];
//        int n = connection.getInputStream().read(buffer, 0, buffer.length);
//
//        if (n > 0) {
//            StringBuilder builder = new StringBuilder();
//            for (int i = 0; i < n; i++) {
//                builder.append((char) buffer[i]);
//            }
//            String message = builder.toString().trim();
//            logger.debug(connection.getSocket().getPort() + ":" + message);
//            data = new DataWrapper("?", message);
//        }
//
//        return data;
//    }

    public Subscriber setErrorListener(ErrorListener listener) {
        this.errListener = listener;
        return this;
    }

    public void cancel() {
        this.isCancel = true;
    }
}
