package handler;

import java.io.IOException;
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
                System.out.println("server recv data");
                // process
                this.oos.writeObject(dataTransfer);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
