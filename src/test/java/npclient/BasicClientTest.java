package npclient;

import npclient.core.command.LoginPublisher;
import npclient.core.command.Publisher;
import npclient.core.command.Subscriber;
import npclient.core.TCPConnection;
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
        TCPConnection user1 = new TCPConnection("np-server.anhdh.me", 1699);
        ObjectOutputStream outputStream1 = new ObjectOutputStream(user1.getOutputStream());
        DataTransfer login1 = new DataTransfer(Constants.INITIALIZE_TOPIC, "Lam", Constants.INIT_COMMAND);
        outputStream1.writeObject(login1);

        TCPConnection user2 = new TCPConnection("np-server.anhdh.me", 1699);
        ObjectOutputStream outputStream2 = new ObjectOutputStream(user2.getOutputStream());
        DataTransfer login2 = new DataTransfer(Constants.INITIALIZE_TOPIC, "HA", Constants.INIT_COMMAND);
        outputStream2.writeObject(login2);

        Thread.sleep(5000);

        DataTransfer dataSub = new DataTransfer("chat/Lam", "HA", Constants.SUBSCRIBE);
        outputStream2.writeObject(dataSub);

        Thread.sleep(5000);

        DataTransfer dataPub = new DataTransfer("chat/HA", "Lam", Constants.PUBLISH, "message");
        outputStream1.writeObject(dataPub);

        Thread.sleep(5000);

        ObjectInputStream inputStream2 = new ObjectInputStream(user2.getInputStream());
        DataTransfer receivedData = (DataTransfer) inputStream2.readObject();
        Assertions.assertEquals("chat/Lam", receivedData.topic);
        Assertions.assertEquals("message", receivedData.data);
    }

    @Test
    public void getActiveUsersTest() throws InterruptedException {
        new Subscriber(Constants.ONLINE_TOPIC, "lamnt")
                                .setNewMessageListener(new SubscribedTopicListener() {
                                    @Override
                                    public void onReceive(DataTransfer message) {
                                        System.out.println("Online = " + message.data);
                                    }
                                })
                                .listen();

        Thread.sleep(5000);

        new Subscriber("chat/L", "lamnt")
                .setNewMessageListener(new SubscribedTopicListener() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        System.out.println("Chat: " + message.data);
                    }
                })
                .listen();

        new Publisher("abc", "lamnt2")
                .putData("abc")
                .post();

        Thread.sleep(150000);
    }

    @Test
    public void asyncChatTest() throws InterruptedException {
        new LoginPublisher("lamnt1")
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        try {
                            Thread.sleep(10000);
                            new Publisher("chat/lamnt2", message.name)
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

        new LoginPublisher("lamnt2")
                .setSuccessListener(new OnPublishMessageSuccess() {
                    @Override
                    public void onReceive(DataTransfer message) {
                        try {
                            Thread.sleep(3000);
                            new Subscriber("chat/lamnt1", message.name)
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

        Thread.sleep(25000);
    }
}
