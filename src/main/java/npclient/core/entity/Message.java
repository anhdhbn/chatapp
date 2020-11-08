package npclient.core.entity;

public class Message implements Comparable {

    private String from;
    private String to;
    private long time;
    private String content;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Message) {
            Message m = (Message) o;
            return Long.compare(time, m.time);
        }
        return 0;
    }
}
