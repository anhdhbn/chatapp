package handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerHandler extends ReadWriteHandler{
    public ServerHandler(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try {
            this.initStream();
            while (true){
                DataTransfer dataTransfer = (DataTransfer) ois.readObject();
                // process
                this.oos.writeObject(dataTransfer);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
