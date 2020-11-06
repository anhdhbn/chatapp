package npclient;

import npclient.command.LoginPublisher;
import npclient.command.Publisher;
import npclient.command.Subscriber;
import npclient.core.Connection;
import npclient.core.callback.OnLoginSuccess;
import npclient.core.callback.OnPublishMessageSuccess;
import npclient.core.callback.SubscribedTopicListener;
import nputils.Constants;
import nputils.DataTransfer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BasicClientTest {

    @Test
    public void singleThreadTest() throws IOException, ClassNotFoundException, InterruptedException {
        Connection user1 = new Connection("np-server.anhdh.me", 1699);
        ObjectOutputStream outputStream1 = new ObjectOutputStream(user1.getOutputStream());
        DataTransfer login1 = new DataTransfer("login", "Lam", Constants.INIT_COMMAND);
        outputStream1.writeObject(login1);

        Connection user2 = new Connection("np-server.anhdh.me", 1699);
        ObjectOutputStream outputStream2 = new ObjectOutputStream(user2.getOutputStream());
        DataTransfer login2 = new DataTransfer("login", "HA", Constants.INIT_COMMAND);
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
        Assertions.assertEquals("test topic", receivedData.topic);
        Assertions.assertEquals("message", receivedData.data);
    }

    @Test
    public void basicChatTest() throws InterruptedException {
        new LoginPublisher("lamnt")
                .setOnLoginSuccess(new OnLoginSuccess() {
                    @Override
                    public void onLogin(String username, Connection connection) {
                        try {
                            Thread.sleep(10000);
                            new Publisher("chat", username)
                                    .putData("Hello")
                                    .setSuccessListener(new OnPublishMessageSuccess() {
                                        @Override
                                        public void onReceive(DataTransfer message) {
                                            System.out.println("Send " + message.data);
                                        }
                                    })
                                    .post();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        System.out.println(message.name + " login success");
                    }
                })
                .post();

        new LoginPublisher("tungtobi")
                .setOnLoginSuccess(new OnLoginSuccess() {
                    @Override
                    public void onLogin(String username, Connection connection) {
                        try {
                            Thread.sleep(3000);
                            new Subscriber("chat", username)
                                    .setNewMessageListener(new SubscribedTopicListener() {
                                        @Override
                                        public void onReceive(DataTransfer message) {
                                            System.out.println("Receive " + message.data);
                                            Assertions.assertEquals("Hello", message.data);
                                        }
                                    })
                                    .listen();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        System.out.println(message.name + " login success");
                    }
                })
                .post();

        Thread.sleep(20000);
    }
}
