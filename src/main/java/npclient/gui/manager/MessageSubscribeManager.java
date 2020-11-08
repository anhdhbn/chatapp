package npclient.gui.manager;

import npclient.core.command.Subscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MessageSubscribeManager extends HashMap<String, Subscriber> {

    private static MessageSubscribeManager instance;

    public static MessageSubscribeManager getInstance() {
        if (instance == null)
            instance = new MessageSubscribeManager();

        return instance;
    }

    public void clearOffline(List<String> online) {
        Set<String> keys = keySet();
        for (String user : keys) {
            if (!online.contains(user)) {
                Subscriber subscriber = remove(user);
                subscriber.cancel();
            }
        }
    }
}
