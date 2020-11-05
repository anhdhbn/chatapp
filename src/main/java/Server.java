import handler.ServerHandler;
import utils.ConfigReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{
    private int port;
    public Server(ConfigReader config) {
        this.port = config.port;
    }

    public void StartServer() throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server is opening on port " + port);
        while (true) {
            Socket socket = server.accept();
            System.out.println(socket.getRemoteSocketAddress() + " connected");
            ServerHandler serverHandler = new ServerHandler(socket);
            serverHandler.start();
        }
    }
}
