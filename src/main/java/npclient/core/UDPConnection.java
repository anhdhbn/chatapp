package npclient.core;

import npclient.CliConstants;

import java.net.*;

public class UDPConnection extends DatagramSocket {

    public UDPConnection() throws SocketException {
        super();
    }



    public static InetAddress getServInetAddr() throws UnknownHostException {
        return InetAddress.getByName(CliConstants.NP_HOST);
    }
}
