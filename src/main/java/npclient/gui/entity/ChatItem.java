package npclient.gui.entity;

import npclient.MyAccount;

public abstract class ChatItem implements Comparable<ChatItem> {

    protected String name;
    protected String lastMessage;
    protected long time;
    protected boolean seen;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public void update(Messages messages) {
        setSeen(messages.isSeen());

        String rawLastMsg = null;
        Message lastMsg = messages.newest();
        if (lastMsg != null) {
            String lastMsgOwner = lastMsg.getFrom().equals(MyAccount.getInstance().getName()) ? "You" : getName();

            if (lastMsg instanceof FileMessage) {
                rawLastMsg = String.format("%s sent you a attachment", lastMsgOwner);
            } else if (lastMsg instanceof TextMessage) {
                rawLastMsg = ((TextMessage) lastMsg).getContent();
            } else if (lastMsg instanceof EmojiMessage) {
                rawLastMsg = String.format("%s sent you a emoji", lastMsgOwner);
            }

            setTime(lastMsg.getTime());
        }

        setLastMessage(rawLastMsg);
    }

    @Override
    public int compareTo(ChatItem o) {
        return Long.compare(time, o.time);
    }
}
