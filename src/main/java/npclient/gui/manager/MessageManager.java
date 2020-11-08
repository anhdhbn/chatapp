package npclient.gui.manager;

import npclient.gui.entity.Message;
import npclient.gui.entity.Messages;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

    public void clearOffline(List<String> online) {
        Set<String> keys = keySet();
        for (String user : keys) {
            if (!online.contains(user))
                remove(user);
        }
    }
}
