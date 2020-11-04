package handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ReadWriteHandler extends HandlerThread{
    private Socket socket;
    public ReadWriteHandler(Socket socket) {
        this.setSocket(socket);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    protected void initStream() throws IOException {
        this.oos = new ObjectOutputStream(this.getSocket().getOutputStream());
        this.ois = new ObjectInputStream(this.getSocket().getInputStream());
    }
}
