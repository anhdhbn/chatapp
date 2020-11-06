package npclient.command;

import npclient.core.Connection;
import nputils.Constants;
import transferable.DataTransfer;

import java.io.IOException;

public class Publisher extends AbstractPublisher {

    public Publisher(String topic, String username) {
        super(topic, username);

    }

    public Publisher putData(Object data) {
        dataTransfer = new DataTransfer(topic, username, Constants.PUBLISH, data.getClass().getName(), data);
        return this;
    }

    @Override
    protected void postProcess(Connection publishConn) throws IOException {
        logger.debug("Close publish connection");
        publishConn.close();
    }
}
