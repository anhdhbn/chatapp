package npclient.core;

import npclient.Constants;

import java.net.*;

public class UDPConnection extends DatagramSocket {

    public UDPConnection() throws SocketException {
        super();
    }

    public static InetAddress getServInetAddr() throws UnknownHostException {
        return InetAddress.getByName(Constants.NP_HOST);
    }
}
