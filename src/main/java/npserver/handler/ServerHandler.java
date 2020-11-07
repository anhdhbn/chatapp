package npserver.handler;

import npserver.utils.HandlerManagement;
import npserver.utils.Helper;
import nputils.Constants;
import nputils.DataTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ServerHandler extends ReadWriteHandler{
    private static final Logger LOGGER = LogManager.getLogger(ServerHandler.class);

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
                else if(data.command.equals(Constants.SUBSCRIBE)) HandlerManagement.subscribeTopic(this, data.topic);
                else if (data.command.equals(Constants.UN_SUBSCRIBE)) HandlerManagement.unsubscribe(this, data.topic);
                else if (data.command.equals(Constants.PUBLISH)){
                    String[] arr = data.topic.split(Constants.SPLITTER);
                    if(arr.length != 2) continue;
                    if(arr[0].equals(Constants.PREFIX_CHAT)){
                        Helper.sendMessPeerToPeer(this, data, arr[1]);
                    } else if (arr[0].equals(Constants.PREFIX_GROUP)){
                        Helper.sendMessToTopic(this, data);
                    }
                }
                else {
                    // UN_KNOWN_COMMAND
                    data = new DataTransfer();
                    data.command = Constants.UN_KNOWN_COMMAND;
                    this.sendObj(data);
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
