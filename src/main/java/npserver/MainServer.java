package npserver;

import npserver.Server;
import npserver.utils.ConfigReader;

import java.io.IOException;

public class MainServer {
    public static void main(String[] args) throws IOException {
        ConfigReader cr = new ConfigReader();
        cr.getPropValues();

        Server server = new Server(cr);
        server.StartServer();
    }
}
