package npclient.core.command;

import nputils.Constants;
import nputils.DataTransfer;

public class Publisher extends TCPPublisher {

    public Publisher(String topic, String username) {
        super(topic, username);
    }

    public Publisher putData(Object data) {
        dataTransfer = new DataTransfer(topic, username, Constants.PUBLISH, data);
        return this;
    }
}
