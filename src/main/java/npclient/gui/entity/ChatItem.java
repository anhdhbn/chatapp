package npclient.gui.entity;

import npclient.MyAccount;
import npclient.gui.view.ChatItemCell;

public abstract class ChatItem implements Comparable<ChatItem> {

    protected String name;
    protected String lastMessage;
    protected long time;
    protected boolean seen = true;

    // for fast rendering
    protected ChatItemCell cell;

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
//        setSeen(messages.isSeen());

        String rawLastMsg = null;
        Message<?> lastMsg = messages.newest();
        if (lastMsg != null) {
            String lastMsgOwner = lastMsg.getFrom();
            if (lastMsgOwner.equals(MyAccount.getInstance().getName()))
                lastMsgOwner = "You";

            if (lastMsg instanceof FileMessage) {
                rawLastMsg = String.format("%s sent a attachment", lastMsgOwner);
            } else if (lastMsg instanceof TextMessage) {
                rawLastMsg = ((TextMessage) lastMsg).getContent();
            } else if (lastMsg instanceof EmojiMessage) {
                rawLastMsg = String.format("%s sent a emoji", lastMsgOwner);
            }

            setTime(lastMsg.getTime());
        }

        setLastMessage(rawLastMsg);
    }

    public ChatItemCell getCell() {
        return cell;
    }

    public void setCell(ChatItemCell cell) {
        this.cell = cell;
    }

    @Override
    public int compareTo(ChatItem o) {
        return Long.compare(time, o.time);
    }
}
