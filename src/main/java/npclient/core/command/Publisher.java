package npclient.core.command;

import npclient.core.TCPConnection;
import nputils.Constants;
import nputils.DataTransfer;

import java.io.IOException;

public class Publisher extends TCPPublisher {

    public Publisher(String topic, String username) {
        super(topic, username);
    }

    public Publisher putData(Object data) {
        dataTransfer = new DataTransfer(topic, username, Constants.PUBLISH, data);
        return this;
    }

    @Override
    protected void postProcess(TCPConnection publishConn) throws IOException {
        logger.debug("Close publish connection");
        publishConn.close();
    }
}
