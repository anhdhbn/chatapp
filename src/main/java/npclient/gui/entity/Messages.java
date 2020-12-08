package npclient.gui.entity;

import nputils.Constants;

import java.util.PriorityQueue;

public class Messages extends PriorityQueue<Message> {

    private final String topic;
    // for fast search
    private ChatItem chatItem;
//    private boolean seen = true;
    private Message newest;

    public Messages(String topic) {
        this.topic = topic;
    }

    @Override
    public boolean add(Message message) {
        if (newest == null || newest.getTime() < message.getTime())
            newest = message;
        return super.add(message);
    }

    public Message newest() {
        return newest;
    }

//    public boolean isSeen() {
//        return seen;
//    }
//
//    public void setSeen(boolean seen) {
//        this.seen = seen;
//    }

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
