package npclient.gui.entity;

import nputils.Constants;

import java.util.PriorityQueue;

public class Messages extends PriorityQueue<Message> {

    private final String topic;
    private boolean seen = false;

    public Messages(String topic) {
        this.topic = topic;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getTopic() {
        return topic;
    }

    public boolean isGroup() {
        return topic.startsWith(Constants.PREFIX_GROUP);
    }
}
