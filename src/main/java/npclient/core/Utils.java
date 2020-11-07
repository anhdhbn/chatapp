package npclient.core;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Utils {
    public static SocketAddress toSocketAddr(String host, int port) throws UnknownHostException {
        return (host != null ?
                new InetSocketAddress(host, port) :
                new InetSocketAddress(InetAddress.getByName(null), port));
    }
}
