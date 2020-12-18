package npclient.gui.entity;

import nputils.Constants;

import java.util.PriorityQueue;

public class Messages extends PriorityQueue<Message> {

    private final String topic;

    // for fast search
    private ChatItem chatItem;

    private Message newest;

    public Messages(String topic) {
        this.topic = topic;
    }

    @Override
    public synchronized boolean add(Message message) {
        if (newest == null || newest.getTime() < message.getTime())
            newest = message;
        return super.add(message);
    }

    public synchronized Message newest() {
        return newest;
    }

    public String getTopic() {
        return topic;
    }

    public boolean isGroup() {
        return topic.startsWith(Constants.PREFIX_GROUP);
    }

    public void setChatItem(ChatItem chatItem) {
        this.chatItem = chatItem;
    }

    public ChatItem getChatItem() {
        return chatItem;
    }
}
