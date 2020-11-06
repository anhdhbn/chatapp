package npserver.utils;

import npserver.handler.ServerHandler;
import nputils.Constants;
import nputils.DataTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Helper {
    private static final Logger LOGGER = LogManager.getLogger(Helper.class);

    public static void sendMessToTopic(ServerHandler from, DataTransfer data){
        String[] arr = data.topic.split(Constants.SPLITTER);
        if(arr.length != 2) return;
        if(arr[0].equals(Constants.PREFIX_CHAT) || arr[0].equals(Constants.PREFIX_GROUP)){
            String topic = arr[1];
            Set<ServerHandler> set = HandlerManagement.getAllSubscribers(topic);
            for(ServerHandler handler: set){
                if (handler.name.equals(from.name)) continue;
                else {
                    handler.sendObj(data);
                    LOGGER.info("{}: Send data from ({}) ==> group ({}) ({}): ({})", from.idSocket, from.name, topic, handler.name, data.data);
                }
            }
        }

    }

    public static void sendOnline(){
        Set<ServerHandler> set = HandlerManagement.getAllSubscribers(Constants.ONLINE_TOPIC);
        ArrayList<String> members = new ArrayList<>();
        for(String member: HandlerManagement.getAllMembers()){
            members.add(member);
        }
        DataTransfer data = new DataTransfer(Constants.ONLINE_TOPIC, "server", null, "Set<String>", members);
        for(ServerHandler handler: set){
            handler.sendObj(data);
            LOGGER.info("Server send online signal ==> ({}): ({}) online", handler.name, members.size());
        }
    }
}
