package npserver.utils;

import npserver.handler.ServerHandler;
import nputils.Constants;
import nputils.DataTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;

public class Helper {
    private static final Logger LOGGER = LogManager.getLogger(Helper.class);
    public static void sendMessPeerToPeer(ServerHandler from, DataTransfer data){
        String partner = data.topic.split("-")[1];
        ServerHandler hPartner = HandlerManagement.getHandlerByName(partner);
        if(hPartner != null) {
            hPartner.sendObj(data);
            LOGGER.info("{}: Send data ({}) ==> ({}): ({})", from.idSocket, from.name, hPartner.name, data.data);
        }
    }

    public static void sendMessToTopic(ServerHandler from, DataTransfer data){
        Set<ServerHandler> set = HandlerManagement.getAllSubscribers(data.topic);
        for(ServerHandler handler: set){
            if (handler.name.equals(from.name)) continue;
            else {
                handler.sendObj(data);
                LOGGER.info("{}: Send data room from ({}) ==> ({}): ({})", from.idSocket, from.name, handler.name, data.data);
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
