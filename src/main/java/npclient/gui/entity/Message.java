package npclient.gui.entity;

import java.util.concurrent.atomic.AtomicReference;

public abstract class Message<T> implements Comparable<Message<T>> {

    private String from;
    private long time;
    private T content;
    private AtomicReference<State> state = new AtomicReference<>(State.SUCCESS);

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

    public State getState() {
        return state.get();
    }

    public void setState(State state) {
        this.state.set(state);
    }

    @Override
    public int compareTo(Message o) {
        return Long.compare(time, o.time);
    }

    public enum State {
        SENDING,
        SUCCESS,
        FAILURE
    }
}
