package nputils;

import java.io.Serializable;

public class DataTransfer implements Serializable {
    public String topic;
    public String name;
    public String command;
    public long datetime;
    public String className;
    public Object data;

    public DataTransfer() {
        this.datetime = System.currentTimeMillis() / 1000L;
    }

    public DataTransfer(String topic, String name, String command) {
        this();
        this.topic = topic;
        this.name = name;
        this.command = command;
    }

    public DataTransfer(String topic, String name, String command, String className, Object data) {
        this();
        this.topic = topic;
        this.name = name;
        this.command = command;
        this.className = className;
        this.data = data;
    }
}
