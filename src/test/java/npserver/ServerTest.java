package npserver;


import npserver.Server;
import npserver.UdpServer;
import npserver.handler.ReadWriteHandler;
import npserver.utils.ConfigReader;
import nputils.Constants;
import nputils.DataTransfer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ServerTest {
    protected ArrayList<Socket> clients;
    protected ArrayList<ReadWriteHandler> handlers;
    protected ArrayList<DatagramSocket> udpConns;

    protected Thread thread;
    protected ConfigReader cr;
    protected final String user = "anhdh";
    protected Server server;
    protected UdpServer udpServer;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
        cr = new ConfigReader();
        cr.getPropValues();
        clients = new ArrayList<>();
        handlers = new ArrayList<>();
        udpConns = new ArrayList<>();

        this.startServer();
        this.delay();
        this.startUdpServer();

        this.delay();
    }

    public void startServer(){
        thread = new Thread(() -> {
            server = new Server(cr);
            try {
                server.StartServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void startUdpServer(){
        thread = new Thread(() -> {
            udpServer = new UdpServer(cr);
            try {
                udpServer.StartServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void generateClient(int n) throws IOException {
        for (int i = 0; i < n; i++) {
            this.generateClient();
        }
    }

    public void generateClientWithName(String name) throws IOException {
        Socket client = new Socket(InetAddress.getLocalHost(), cr.port);
        ReadWriteHandler handler = new ReadWriteHandler(client);
        handler.initStream();
        handler.name = name;
        DataTransfer dataInit = new DataTransfer(null, name, Constants.INIT_COMMAND);
        handler.sendObj(dataInit);


        String topic = Constants.PREFIX_LOGIN + Constants.SPLITTER + handler.name;
        DataTransfer dataLogin = new DataTransfer(topic, handler.name, Constants.PUBLISH);
        handler.sendObj(dataLogin);
        dataLogin = handler.receiveObj();
        Assertions.assertEquals(true, (boolean)dataLogin.data);

        DatagramSocket udp = login(handler.name); // udp


        // subscribe voice
        topic = Constants.PREFIX_VOICE + Constants.SPLITTER + handler.name;
        DataTransfer dataSubVoice = new DataTransfer(topic, handler.name, Constants.SUBSCRIBE);
        handler.sendObj(dataSubVoice);

        handlers.add(handler);
        clients.add(client);
        udpConns.add(udp);
    }

    public DatagramSocket login(String user) throws IOException {
        DatagramSocket udpConn = new DatagramSocket();
        byte[] nameBytes = user.getBytes();
        byte[] initBuf = new byte[Constants.BUFFER_SIZE];
        initBuf[0] = (byte) user.length();
        System.arraycopy(nameBytes, 0, initBuf, 1, nameBytes.length);

        DatagramPacket initPacket = new DatagramPacket(initBuf, initBuf.length,
                InetAddress.getByName("localhost"),
                cr.portUdp
        );

        udpConn.send(initPacket);
        return udpConn;
    }

    public void generateClient() throws IOException {
        String userName = this.user + (this.handlers.size() + 1);
        this.generateClientWithName(userName);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
        for(ReadWriteHandler handler :this.handlers){
            handler.closeAll();
        }
        this.server.server.close();
        this.udpServer.server.close();
    }

    public void delay() throws InterruptedException {
        Thread.sleep(3000);
    }

    public String generateChatTopic(String user){
        return Constants.PREFIX_CHAT + Constants.SPLITTER + user;
    }

    public String generateGroupTopic(String topic){
        return Constants.PREFIX_GROUP + Constants.SPLITTER + topic;
    }
}