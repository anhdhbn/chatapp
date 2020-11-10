package npclient.gui.entity;

public abstract class Message<T> implements Comparable<Message<T>> {

    private String from;
    private long time;
    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

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
