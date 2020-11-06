package transferable;

import java.io.Serializable;

public class DataTransfer implements Serializable {
    private static final long serialVersionUID = 1L;
    public String topic;
    public String name;
    public String command;
    public long datetime;
    public Object data;

    public DataTransfer() {
        this.datetime = System.currentTimeMillis();
    }

    public DataTransfer(String topic, String name, String command) {
        this();
        this.topic = topic;
        this.name = name;
        this.command = command;
    }

    public DataTransfer(String topic, String name, String command, Object data) {
        this();
        this.topic = topic;
        this.name = name;
        this.command = command;
        this.data = data;
    }
}
