package npserver.utils;

import npserver.handler.ServerHandler;
import nputils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HandlerManagement {
    private static final Logger LOGGER = LogManager.getLogger(HandlerManagement.class);

    private static Map<String, Set<ServerHandler>> socketDic = new HashMap<String, Set<ServerHandler>>();
    private static Map<String, Set<ServerHandler>> topics = new HashMap<String, Set<ServerHandler>>();


    public static void addNewHandler(String name, ServerHandler client){
        if (socketDic.containsKey(name)) {
            Set<ServerHandler> set = socketDic.get(name);
            set.add(client);
        }
        else {
            Set<ServerHandler> set = new HashSet<>();
            set.add(client);
            socketDic.put(name, set);
            Helper.sendOnline();
        }
        LOGGER.info("{}: ({})'s handler was added, current number handlers of this user ({})", client.idSocket, client.name, socketDic.get(name).size());
    }

    public static void removeHandler(String name, ServerHandler client){
        if (socketDic.containsKey(name)){
            Set<ServerHandler> set = socketDic.get(name);
            if(set.size() > 0) {
                set.remove(client);
                LOGGER.info("{}: ({})'s handler was removed", client.idSocket, client.name);
            }
            if(set.size() == 0) {
                socketDic.remove(name, set);
                Helper.sendOnline();
                UdpConnManagement.tcpRemovePair(name);
            }
        }
    }

    public static Set<String> getAllMembers(){
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
        if(topic.equals(Constants.ONLINE_TOPIC)) Helper.sendOnline();
        LOGGER.info("{}: ({}) subscribed ==> ({})", client.idSocket, client.name, topic);
    }

    public static void unsubscribe(ServerHandler client, String topic){
        if(topics.containsKey(topic)){
            topics.get(topic).remove(client);
            LOGGER.info("{}: ({}) unsubscribed ==> ({})", client.idSocket, client.name, topic);
        }
    }

    public static void unsubscribeIfExist(ServerHandler client){
        for(String topic: topics.keySet()){
            Set<ServerHandler> set = topics.get(topic);
            if(set.contains(client)){
                set.remove(client);
                LOGGER.info("{}: ({}) unsubscribed ==> ({})", client.idSocket, client.name, topic);
            }
        }
    }
}
