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

    public static void sendMessPeerToPeer(ServerHandler from, DataTransfer data, String to, String prefix){
        // A sub chat/B
        // B sub chat/A
        // from A publish topic: chat/B
        // get all subscribers topic: chat/A
        // find a handler named B
        String topic = prefix + Constants.SPLITTER + from.name; // chat/A
        Set<ServerHandler> set = HandlerManagement.getAllSubscribers(topic);
        for(ServerHandler handler: set){
            if (handler.name.equals(to)) {
                data.topic = topic;
                handler.sendObj(data);
                LOGGER.info("{}: Send data ({}) ==> ({}): ({})", from.idSocket, from.name, handler.name, data.data);
            }
        }
    }

    public static void sendMessToTopic(ServerHandler from, DataTransfer data){
        Set<ServerHandler> set = HandlerManagement.getAllSubscribers(data.topic);
        for(ServerHandler handler: set){
            if (handler.name.equals(from.name)) continue;
            else {
                handler.sendObj(data);
                LOGGER.info("{}: Send data from ({}) ==> group ({}) ({}): ({})", from.idSocket, from.name, data.topic, handler.name, data.data);
            }
        }
    }

    public static void sendOnline(){
        Set<ServerHandler> set = HandlerManagement.getAllSubscribers(Constants.ONLINE_TOPIC);
        ArrayList<String> members = new ArrayList<>();
        for(String member: HandlerManagement.getAllMembers()){
            members.add(member);
        }
        DataTransfer data = new DataTransfer(Constants.ONLINE_TOPIC, "server", null, members);
        for(ServerHandler handler: set){
            handler.sendObj(data);
            LOGGER.info("Server send online signal ==> ({}): ({}) online", handler.name, members.size());
        }
    }

    public static boolean checkExistUser(String username){
        Set<ServerHandler> set = HandlerManagement.getAllSubscribers(Constants.ONLINE_TOPIC);
        return set.stream().anyMatch(h-> h.name.equals(username));
    }
}
