package utils;

import handler.ServerHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HandlerManagement {
    private static final Logger LOGGER = LogManager.getLogger(HandlerManagement.class);

    private static Map<String, ServerHandler> socketDic = new HashMap<String, ServerHandler>();
    private static Map<String, Set<ServerHandler>> topics = new HashMap<String, Set<ServerHandler>>();

    public static ServerHandler getHandlerByName(String name) {
        return socketDic.get(name);
    }

    public static boolean addNewHandler(String name, ServerHandler client){
        if (socketDic.containsKey(name)) return false;
        else {
            socketDic.put(name, client);
            LOGGER.info("{}: {}'s handler was added", client.idSocket, client.name);
            return true;
        }
    }

    public static void removeHandler(String name, ServerHandler client){
        if (socketDic.containsKey(name)){
            socketDic.remove(name, client);
            LOGGER.info("{}: {}'s handler was removed", client.idSocket, client.name);
        }
    }

    public static Set<String> getAllKey(){
        return socketDic.keySet();
    }

    public static boolean checkContainKey(String name){
        return  socketDic.containsKey(name);
    }

    public static Set<ServerHandler> getAllSubscribers(String topic){
        Set<ServerHandler> set = topics.get(topic);
        if(set == null) set = new HashSet<>();
        return set;
    }

    public static void subscribeTopic(ServerHandler client, String topic){
        if(topics.containsKey(topic)){
            topics.get(topic).add(client);
        } else {
            Set<ServerHandler> set = new HashSet<ServerHandler>();
            set.add(client);
            topics.put(topic, set);
        }
        LOGGER.info("{}: {} subscribed ==> {}", client.idSocket, client.name, topic);
    }

    public static void unsubscribe(ServerHandler client, String topic){
        if(topics.containsKey(topic)){
            topics.get(topic).remove(client);
            LOGGER.info("{}: {} unsubscribed ==> {}", client.idSocket, client.name, topic);
        }
    }

    public static void unsubscribeIfExist(ServerHandler client){
        for(String topic: topics.keySet()){
            Set<ServerHandler> set = topics.get(topic);
            if(set.contains(client)){
                set.remove(client);
                LOGGER.info("{}: {} unsubscribed ==> {}", client.idSocket, client.name, topic);
            }
        }
    }
}
