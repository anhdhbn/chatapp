package npserver;

import npserver.Server;
import npserver.utils.ConfigReader;

import java.io.IOException;

public class MainServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigReader cr = new ConfigReader();
        cr.getPropValues();

        Thread udpServerThread = new Thread(()-> {
            UdpServer udpserver = new UdpServer(cr);
            try {
                udpserver.StartServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        udpServerThread.start();
        Server server = new Server(cr);
        server.StartServer();
    }
}
