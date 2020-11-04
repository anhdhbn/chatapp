package handler;

import java.io.Serializable;

public class DataTransfer implements Serializable {
    public String topic;
    public String from;
    public long datetime;
    public String className;
    public Object data;
}
