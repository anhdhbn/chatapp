package npserver;

import npserver.handler.ServerHandler;
import npserver.utils.ConfigReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server{
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private int port;
    public ServerSocket server;
    public Server(ConfigReader config) {
        this.port = config.port;
    }

    public void StartServer() throws IOException {
        server = new ServerSocket(port);
        LOGGER.info("Server is opening on port {}", port);
        while (true) {
            Socket socket = server.accept();
            ServerHandler serverHandler = new ServerHandler(socket);
            serverHandler.start();
        }
    }
}
