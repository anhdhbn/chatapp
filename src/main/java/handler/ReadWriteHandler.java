package handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteHandler extends HandlerThread{
    private Socket socket;
    private Lock lock;
    public ReadWriteHandler(Socket socket) {
        this.setSocket(socket);
        this.lock = new ReentrantLock(true);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void initStream() throws IOException {
        this.oos = new ObjectOutputStream(this.getSocket().getOutputStream());
        this.ois = new ObjectInputStream(this.getSocket().getInputStream());
    }

    public void sendObj(DataTransfer data) {
        this.lock.lock();
        try {
            this.oos.writeObject(data);
        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            this.lock.unlock();
        }
    }
    
    public DataTransfer receiveObj(){
        DataTransfer data = null;
        this.lock.lock();
        try {
            data = (DataTransfer)this.ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.lock.unlock();
            return data;
        }
    }
}
