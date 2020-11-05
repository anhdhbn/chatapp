package utils;

import handler.ServerHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HandlerManagement {
    private static Map<String, ServerHandler> socketDic = new HashMap<String, ServerHandler>();
    private static Map<String, Set<ServerHandler>> topics = new HashMap<String, Set<ServerHandler>>();

    public static ServerHandler getHandlerByName(String name) {
        return socketDic.get(name);
    }

    public static boolean addNewHandler(String name, ServerHandler serverHandler){
        if (socketDic.containsKey(name)) return false;
        else {
            socketDic.put(name, serverHandler);
            return true;
        }
    }

    public static void removeHandler(String name, ServerHandler serverHandler){
        if (socketDic.containsKey(name)){
            socketDic.remove(name, serverHandler);
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
        System.out.println("[INFO] " + client.name + " sub ==> " + topic);
        if(topics.containsKey(topic)){
            topics.get(topic).add(client);
        } else {
            Set<ServerHandler> set = new HashSet<ServerHandler>();
            set.add(client);
            topics.put(topic, set);
        }
    }

    public static void unsubscribe(ServerHandler client, String topic){
        if(topics.containsKey(topic)){
            topics.get(topic).remove(client);
        }
    }

    public static void unsubscribeIfExist(ServerHandler client){
        for(String topic: topics.keySet()){
            Set<ServerHandler> set = topics.get(topic);
            if(set.contains(client)){
                set.remove(client);
            }
        }
    }
}
