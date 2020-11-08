package npserver;

import npserver.handler.ServerHandler;
import npserver.utils.ConfigReader;
import npserver.utils.UdpConnManagement;
import nputils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class UdpServer {
    private static final Logger LOGGER = LogManager.getLogger(UdpServer.class);
    private int port;
    public DatagramSocket server;
    public UdpServer(ConfigReader config) {
        this.port = config.portUdp;
}

    public void StartServer() throws IOException {
        server = new DatagramSocket(port);
        LOGGER.info("Udp server is opening on port {}", port);
        byte[] recvData = new byte[Constants.BUFFER_SIZE];
        while (true) {

            DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);
            if(recvPacket.getAddress() == null) continue;

            String strAdr = recvPacket.getAddress().getHostAddress();
            LOGGER.info("Server: Recv package from {}", strAdr);
            int port = recvPacket.getPort();
            String ipAdr = UdpConnManagement.createAddr(strAdr, port);

            String sender = UdpConnManagement.getUserByConn(ipAdr);
            if(sender == null){
                String username = new String(recvPacket.getData());
                UdpConnManagement.addMapping(username, recvPacket.getAddress(), port);
            } else {
                UdpConnManagement.IPInfo ipInfo = UdpConnManagement.getPartnerIpInfo(sender);

                DatagramPacket sendPacket = new DatagramPacket(recvData, recvData.length, ipInfo.host, ipInfo.port);
                server.send(sendPacket);
                String partnerAdr = UdpConnManagement.createAddr(ipInfo.host.getHostAddress(), ipInfo.port);
                LOGGER.info("{}: {} forward to {}", ipAdr, sender, partnerAdr);
            }
        }
    }
}
