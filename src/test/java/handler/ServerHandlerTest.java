package handler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import utils.ConfigReader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ServerHandlerTest {

    private static ServerSocket serverSocket;
    private static Socket client;
    private static ReadWriteHandler handler;
    private static Thread thread;
    private static ConfigReader cr;

    @org.junit.jupiter.api.BeforeAll
    public static void setUp() throws Exception {
        cr = new ConfigReader();
        cr.getPropValues();

        serverSocket = new ServerSocket(cr.port);
        System.out.println("Server is opening on port " + cr.port);

        thread = new Thread(() -> {
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    System.out.println(socket.getRemoteSocketAddress() + " connected");
                    ServerHandler serverHandler = new ServerHandler(socket);
                    serverHandler.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        client = new Socket(InetAddress.getLocalHost(), cr.port);
        handler = new ReadWriteHandler(client);
        handler.initStream();
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDown() throws Exception {
        serverSocket.close();
    }

    @Test
    public void testEcho() throws IOException, ClassNotFoundException {
        DataTransfer data = new DataTransfer();
        data.className = "String";

        handler.oos.writeObject(data);
        DataTransfer data2 = (DataTransfer)handler.ois.readObject();
        client.close();
        Assertions.assertEquals(data.className, data2.className);
    }
}