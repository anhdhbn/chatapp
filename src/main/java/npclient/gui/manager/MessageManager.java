package npclient.gui.manager;

import npclient.gui.entity.Message;
import npclient.gui.entity.Messages;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MessageManager implements Map<String, Messages> {

    private static MessageManager instance;

    private final AtomicReference<Map<String, Messages>> ref;

    public MessageManager() {
        this.ref = new AtomicReference<>(new HashMap<>());
    }

    public static MessageManager getInstance() {
        if (instance == null)
            instance = new MessageManager();
        return instance;
    }

    public synchronized Messages append(String name, Message m) {
        Messages messages = get(name);

        if (messages == null) {
            messages = new Messages(name);
            put(name, messages);
        }

        Message lastMsg = messages.peek();
        if (lastMsg != null && lastMsg.getFrom().equals(m.getFrom()) && lastMsg.getTime() == m.getTime()) {
            // duplicate data ????
        } else
            messages.add(m);

        return messages;
    }

    public synchronized void clearOffline(List<String> online) {
        Set<String> keys = keySet();
        for (String user : keys) {
            if (!online.contains(user))
                remove(user);
        }
    }

    @Override
    public int size() {
        return ref.get().size();
    }

    @Override
    public boolean isEmpty() {
        return ref.get().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return ref.get().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return ref.get().containsKey(value);
    }

    @Override
    public Messages get(Object key) {
        return ref.get().get(key);
    }

    @Override
    public Messages put(String key, Messages value) {
        return ref.get().put(key, value);
    }

    @Override
    public Messages remove(Object key) {
        return ref.get().remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Messages> m) {
        ref.get().putAll(m);
    }

    @Override
    public void clear() {
        ref.get().clear();
    }

    @Override
    public Set<String> keySet() {
        return ref.get().keySet();
    }

    @Override
    public Collection<Messages> values() {
        return ref.get().values();
    }

    @Override
    public Set<Entry<String, Messages>> entrySet() {
        return ref.get().entrySet();
    }
}
