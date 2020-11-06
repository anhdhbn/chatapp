package npserver.handler;


import npserver.Server;
import npserver.utils.ConfigReader;
import nputils.Constants;
import transferable.DataTransfer;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ServerHandlerTest {
    protected ArrayList<Socket> clients;
    protected ArrayList<ReadWriteHandler> handlers;

    protected Thread thread;
    protected ConfigReader cr;
    protected final String user = "anhdh";
    protected Server server;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
        cr = new ConfigReader();
        cr.getPropValues();
        clients = new ArrayList<>();
        handlers = new ArrayList<>();

        thread = new Thread(() -> {
            server = new Server(cr);
            try {
                server.StartServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        this.delay();
    }

    public void generateClient(int n) throws IOException {
        for (int i = 0; i < n; i++) {
            this.generateClient();
        }
    }

    public void generateClient() throws IOException {
        Socket client = new Socket(InetAddress.getLocalHost(), cr.port);
        ReadWriteHandler handler = new ReadWriteHandler(client);
        handler.initStream();
        String userName = this.user + (this.handlers.size() + 1);
        handler.name = userName;
        DataTransfer dataInit = new DataTransfer(null, userName, Constants.INIT_COMMAND);
        handler.sendObj(dataInit);

        handlers.add(handler);
        clients.add(client);
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
        for(ReadWriteHandler handler :this.handlers){
            handler.closeAll();
        }
        this.server.server.close();
    }

    public void delay() throws InterruptedException {
        Thread.sleep(3000);
    }
}