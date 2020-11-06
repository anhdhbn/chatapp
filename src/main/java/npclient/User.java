package npclient;

import npclient.core.Connection;
import transferable.DataTransfer;
import nputils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class User {

    private String name;

    private Connection connection;

    public User(String name, Connection connection) {
        this.name = name;
        this.connection = connection;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Connection user1 = new Connection("np-server.anhdh.me", 1699);
        ObjectOutputStream outputStream1 = new ObjectOutputStream(user1.getOutputStream());
        DataTransfer login1 = new DataTransfer(null, "Lam", Constants.INIT_COMMAND);
        outputStream1.writeObject(login1);

        Connection user2 = new Connection("np-server.anhdh.me", 1699);
        ObjectOutputStream outputStream2 = new ObjectOutputStream(user2.getOutputStream());
        DataTransfer login2 = new DataTransfer(null, "HA", Constants.INIT_COMMAND);
        outputStream2.writeObject(login2);

        Thread.sleep(5000);

        DataTransfer dataSub = new DataTransfer("test topic", "HA", Constants.SUBSCRIBE);
        outputStream2.writeObject(dataSub);

        Thread.sleep(5000);

        DataTransfer dataPub = new DataTransfer("test topic", "Lam", Constants.PUBLISH, String.class.getName(), "message");
        outputStream1.writeObject(dataPub);

        Thread.sleep(5000);

        ObjectInputStream inputStream2 = new ObjectInputStream(user2.getInputStream());
        DataTransfer receivedData = (DataTransfer) inputStream2.readObject();
        System.out.println("Topic=" + receivedData.topic + ",Data=" + receivedData.data);
    }
}
