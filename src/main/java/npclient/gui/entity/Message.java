package npclient.gui.entity;

public abstract class Message implements Comparable<Message> {

    private String from;
    private long time;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int compareTo(Message o) {
        return Long.compare(time, o.time);
    }
}
