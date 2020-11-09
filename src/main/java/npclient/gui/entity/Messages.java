package npclient.gui.entity;

import java.util.PriorityQueue;

public class Messages extends PriorityQueue<Message> {

    private final String name;
    private boolean seen = false;

    public Messages(String name) {
        this.name = name;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getName() {
        return name;
    }
}
