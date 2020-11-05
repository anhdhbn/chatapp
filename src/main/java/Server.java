import handler.ServerHandler;
import utils.ConfigReader;
import utils.HandlerManagement;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server{
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private int port;
    public Server(ConfigReader config) {
        this.port = config.port;
    }

    public void StartServer() throws IOException {
        ServerSocket server = new ServerSocket(port);
        LOGGER.info("Server is opening on port {}", port);
        while (true) {
            Socket socket = server.accept();
            System.out.println(socket.getRemoteSocketAddress() + " connected");
            ServerHandler serverHandler = new ServerHandler(socket);
            serverHandler.start();
        }
    }
}
