import handler.ServerHandler;
import utils.ConfigReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    private int port;

    public MainServer(ConfigReader config) {
        this.port = config.port;
    }

    private void execute() throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server is opening on port " + port);
        while (true) {
            Socket socket = server.accept();
            System.out.println(socket.getRemoteSocketAddress() + " connected");
            ServerHandler serverHandler = new ServerHandler(socket);
            serverHandler.start();
        }
    }

    public static void main(String[] args) throws IOException {
        ConfigReader cr = new ConfigReader();
        cr.getPropValues();

        MainServer server = new MainServer(cr);
        server.execute();
    }
}
