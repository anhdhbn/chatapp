package npclient.core;

import npclient.core.callback.ErrorListener;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.logger.CliLogger;
import npserver.handler.DataTransfer;
import npserver.utils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Publisher implements Runnable {

    private static final CliLogger logger = CliLogger.get(Publisher.class);

    private final Connection connection;

    private String content;

    private final String topic;

    private ErrorListener errorListener;

    private OnPublishMessageSuccess successListener;

    public Publisher(Connection connection, String topic) {
        this.connection = connection;
        this.topic = topic;
    }

    public void post(String content) {
        this.content = content;
        String threadName = "Publisher " + topic;
        new Thread(this, threadName).start();
    }

    public Publisher setErrorListener(ErrorListener listener) {
        this.errorListener = listener;
        return this;
    }

    public Publisher setSuccessListener(OnPublishMessageSuccess listener) {
        this.successListener = listener;
        return this;
    }

    @Override
    public void run() {
        try {
            synchronized (connection.getWriter()) {
                logger.debug("Publish to topic " + topic);

                ObjectOutputStream outputStream = new ObjectOutputStream(connection.getSocket().getOutputStream());
                outputStream.writeObject(new DataTransfer("L-A", "name", Constants.PUBLISH));
//                Writer writer = connection.getWriter();
//                writer.write(content);
//                writer.flush();

                Connection connection2 = new Connection("np-server.anhdh.me", 1699);
                ObjectOutputStream outputStream2 = new ObjectOutputStream(connection2.getSocket().getOutputStream());
                outputStream2.writeObject(new DataTransfer("L-A", "name", Constants.SUBSCRIBE));

                ObjectInputStream inputStream = new ObjectInputStream(connection2.getInputStream());
                DataTransfer dataTransfer = (DataTransfer) inputStream.readObject();
                logger.debug("Topic=" + dataTransfer.topic);

                if (successListener != null) {
                    logger.debug("On Success Callback");
                    successListener.onReceive(content);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage());
            if (errorListener != null) {
                logger.debug("On Error Callback");
                errorListener.onReceive(e);
            } else {
                e.printStackTrace();
            }
        }

    }
}
