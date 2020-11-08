package npclient.core.data;

import npclient.core.entity.Message;

import java.util.HashMap;

public class MessageManager extends HashMap<String, Messages> {

    private static MessageManager instance;

    public static MessageManager getInstance() {
        if (instance == null)
            instance = new MessageManager();
        return instance;
    }

    public Messages append(String name, Message m) {
        Messages messages = get(name);

        if (messages == null) {
            messages = new Messages();
            put(name, messages);
        }

        messages.add(m);

        return messages;
    }
}
