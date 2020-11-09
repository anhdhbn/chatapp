package npclient.gui.manager;

import npclient.core.command.Subscriber;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MessageSubscribeManager implements Map<String, Subscriber> {

    private static MessageSubscribeManager instance;

    private final AtomicReference<Map<String, Subscriber>> ref;

    public static MessageSubscribeManager getInstance() {
        if (instance == null)
            instance = new MessageSubscribeManager();

        return instance;
    }

    public MessageSubscribeManager() {
        ref = new AtomicReference<>();
    }

    public synchronized void clearOffline(List<String> online) {
        Set<String> keys = keySet();
        for (String user : keys) {
            if (!online.contains(user)) {
                Subscriber subscriber = remove(user);
                subscriber.cancel();
            }
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
    public Subscriber get(Object key) {
        return ref.get().get(key);
    }

    @Override
    public Subscriber put(String key, Subscriber value) {
        return ref.get().put(key, value);
    }

    @Override
    public Subscriber remove(Object key) {
        return ref.get().remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Subscriber> m) {
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
    public Collection<Subscriber> values() {
        return ref.get().values();
    }

    @Override
    public Set<Entry<String, Subscriber>> entrySet() {
        return ref.get().entrySet();
    }
}
