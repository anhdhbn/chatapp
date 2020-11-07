package npclient.core;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPConnection extends DatagramSocket {

    public UDPConnection() throws SocketException {
        super();
    }

    public UDPConnection(int port, InetAddress laddr) throws SocketException {
        super(port, laddr);
    }
}
