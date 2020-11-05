package npserver.handler;

import nputils.DataTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import nputils.Constants;
import npserver.utils.HandlerManagement;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Set;

public class ServerHandler extends ReadWriteHandler{
    private static final Logger LOGGER = LogManager.getLogger(ServerHandler.class);
    private boolean switchAudio = false;


    public ServerHandler(Socket socket) {
        super(socket);
        LOGGER.info("{}: connected", this.idSocket);
    }

    private boolean checkName(DataTransfer data){
        if(data.command.equals(Constants.INIT_COMMAND)) return true;
        else return HandlerManagement.checkContainKey(data.name);
    }

    @Override
    public void run() {
        try {
            this.initStream();
            while (true){
                if(!switchAudio){
                    DataTransfer data = (DataTransfer) ois.readObject();
                    LOGGER.info("{}: Recv from ({}) with command ({}) with topic ({}) data ({})", this.idSocket, data.name, data.command, data.topic, data.data);
                    if(!this.checkName(data)) break;
                    if(data.command.equals(Constants.INIT_COMMAND)){
                        if(data.name.isEmpty()) break;
                        else{
                            this.name = data.name;
                            HandlerManagement.addNewHandler(data.name, this);
                        }
                    }
                    else if(data.command.equals(Constants.SWITCH_AUDIO)) this.switchAudio = true;
                    else if(data.command.equals(Constants.SUBSCRIBE)) HandlerManagement.subscribeTopic(this, data.topic);
                    else if (data.command.equals(Constants.UN_SUBSCRIBE)) HandlerManagement.unsubscribe(this, data.topic);
                    else if (data.command.equals(Constants.PUBLISH)){
                        if(data.topic.contains("-")){
                            String partner = data.topic.split("-")[1];
                            ServerHandler hPartner = HandlerManagement.getHandlerByName(partner);
                            if(hPartner != null) {
                                hPartner.sendObj(data);
                                LOGGER.info("{}: Send data ({}) ==> ({}): ({})", this.idSocket, this.name, hPartner.name, data.data);
                            }
                        }else {
                            Set<ServerHandler> set = HandlerManagement.getAllSubscribers(data.topic);
                            for(ServerHandler handler: set){
                                if (handler.name.equals(this.name)) continue;
                                else {
                                    handler.sendObj(data);
                                    LOGGER.info("{}: Send data room from ({}) ==> ({}): ({})", this.idSocket, this.name, handler.name, data.data);
                                }
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
            LOGGER.error("{}: error: ({})", this.idSocket, e.toString());
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
        return Objects.equals(name, handler.name) && Objects.equals(idSocket, handler.idSocket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, idSocket);
    }

    @Override
    public void closeAll() {
        super.closeAll();
        HandlerManagement.removeHandler(this.name, this);
        HandlerManagement.unsubscribeIfExist(this);
    }
}
