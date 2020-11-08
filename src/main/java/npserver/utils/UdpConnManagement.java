package npserver.utils;

import npserver.UdpServer;
import npserver.handler.ServerHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class UdpConnManagement {
    private static final Logger LOGGER = LogManager.getLogger(UdpConnManagement.class);
    public static class IPInfo{
        public InetAddress host;
        public int port;

        public IPInfo(InetAddress host, int port) {
            this.host = host;
            this.port = port;
        }
    }

    private static Map<String, String> userUdpConn = new HashMap<String, String>(); // inet add - username

    private static Map<String, String> pairs = new HashMap<String, String>(); // username, username2
    private static Map<String, IPInfo> userAddr = new HashMap<String, IPInfo>(); // username, info udp

    public static String createAddr(String host, int port){
        return String.format("/%s:%d", host, port);
    }


    public static String getUserByConn(String addr){
        return userUdpConn.get(addr);
    }

    public static void addMapping(String username, InetAddress host, int port){
        String strAdd = createAddr(host.getHostAddress(), port);
        IPInfo ipInfo = new IPInfo(host, port);
        LOGGER.info("{}: map  ==> {}", strAdd, username);
        userUdpConn.put(strAdd, username);
        userAddr.put(username, ipInfo);
    }

    public static IPInfo getPartnerIpInfo(String sender){
        String partner = pairs.get(sender);
        if(partner == null) return null;
        return userAddr.get(partner);
    }
}
