package handler;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import utils.ConfigReader;
import utils.Constants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ServerHandlerTest {

    protected ServerSocket serverSocket;
    protected Socket client;
    protected ReadWriteHandler handler;
    protected Socket client2;
    protected ReadWriteHandler handler2;
    protected Thread thread;
    protected ConfigReader cr;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
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
//                    e.printStackTrace();
                }
            }
        });
        thread.start();
        client = new Socket(InetAddress.getLocalHost(), cr.port);
        handler = new ReadWriteHandler(client);
        handler.initStream();
        DataTransfer dataInit = new DataTransfer(null, "anhdh", Constants.INIT_COMMAND);
        handler.sendObj(dataInit);

        client2 = new Socket(InetAddress.getLocalHost(), cr.port);
        handler2 = new ReadWriteHandler(client2);
        handler2.initStream();
        DataTransfer dataInit2 = new DataTransfer(null, "anhdh2", Constants.INIT_COMMAND);
        handler2.sendObj(dataInit2);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
        serverSocket.close();
        client.close();
        handler.closeAll();
        client2.close();
        handler2.closeAll();
    }
}