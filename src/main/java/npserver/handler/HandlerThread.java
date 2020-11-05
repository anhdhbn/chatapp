package npserver.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HandlerThread extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(HandlerThread.class);
    public String idSocket;
    protected Socket socket;
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;

    public HandlerThread(Socket socket) {
        this.socket = socket;
        this.idSocket = socket.getRemoteSocketAddress().toString();
    }

    private void closeSocket(Socket socket) throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

    private void closeStream(ObjectInputStream objectInputStream) throws IOException {
        if (objectInputStream != null) {
            objectInputStream.close();
        }
    }

    private void closeStream(ObjectOutputStream objectOutputStream) throws IOException {
        if (objectOutputStream != null) {
            objectOutputStream.close();
        }
    }

    public void closeAll() {
        try {
            this.closeSocket(this.socket);
            this.closeStream(this.ois);
            this.closeStream(this.oos);
        } catch (IOException e) {
            LOGGER.error("{}: error: ({})", this.idSocket, e.getMessage());
        }
    }
}
