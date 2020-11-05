package handler;

import utils.HandlerManagement;
import utils.Constants;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerHandler extends ReadWriteHandler{
    private boolean isInit = true;
    private boolean switchAudio = false;
    public String name;

    public ServerHandler(Socket socket) {
        super(socket);
    }

    private boolean checkName(DataTransfer dataTransfer){
        return HandlerManagement.checkContainKey(dataTransfer.name);
    }

    @Override
    public void run() {
        try {
            this.initStream();
            while (true){
                if(!switchAudio){
                    DataTransfer data = (DataTransfer) ois.readObject();
                    if(isInit) {
                        isInit = false;
                        HandlerManagement.addNewHandler(data.name, this);
                        this.name = data.name;
                    }
                    else {
                        if(!this.checkName(data)) break;
                    }

                    if(data.command.equals(Constants.SWITCH_AUDIO)) this.switchAudio = true;
                    else if(data.command.equals(Constants.SUBSCRIBE)) HandlerManagement.subscribeTopic(this, data.topic);
                    else if (data.command.equals(Constants.UN_SUBSCRIBE)) HandlerManagement.unsubscribe(this, data.topic);
                    else if (data.command.equals(Constants.PUBLISH)){
                        if(data.topic.contains("|")){
                            String partner = data.topic.split("|")[1];
                            ServerHandler hPartner = HandlerManagement.getHandlerByName(partner);
                            if(hPartner != null) hPartner.sendObj(data);
                        }else {
                            Set<ServerHandler> set = HandlerManagement.getAllSubscribers(data.topic);
                            for(ServerHandler handler: set){
                                if (handler.name.equals(this.name)) continue;
                                else handler.sendObj(data);
                            }
                        }
                    }
                    else {
                        // error
                        // UN_KNOWN_COMMAND
                        data = new DataTransfer();
                        data.command = Constants.UN_KNOWN_COMMAND;
                        this.sendObj(data);
                    }
                } else {
                    // audio here
                    // code here
                    this.switchAudio = false;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
        }
        finally {
            this.closeAll();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerHandler handler = (ServerHandler) o;
        return Objects.equals(name, handler.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isInit, name);
    }

    @Override
    public void closeAll() {
        super.closeAll();
        HandlerManagement.removeHandler(this.name, this);
        HandlerManagement.unsubscribeIfExist(this);
    }
}
