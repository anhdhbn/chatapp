package npserver;

import npserver.utils.ConfigReader;
import npserver.utils.UdpConnManagement;
import nputils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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

        while (true) {
            byte[] recvData = new byte[Constants.BUFFER_SIZE];
            DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);
            server.receive(recvPacket);

            String strAdr = recvPacket.getAddress().getHostAddress();
            LOGGER.info("Server: Recv package from ({})", strAdr);
            int port = recvPacket.getPort();
            String ipAdr = UdpConnManagement.createAddr(strAdr, port);

            boolean isRegisterSignal = recvPacket.getData()[0] == 0;

            if(isRegisterSignal){
                LOGGER.info("{}: Server recv register signal: ({})", ipAdr, new String(recvPacket.getData()));
                int len = recvPacket.getData()[1];
                if(len > 0){
                    byte[] newBuff = new byte[len];
                    System.arraycopy(recvPacket.getData(), 2, newBuff, 0, len);
                    String username = new String(newBuff);
                    UdpConnManagement.addMapping(username, recvPacket.getAddress(), port);
//                    DatagramPacket sendPacket = new DatagramPacket(recvData, recvData.length, recvPacket.getAddress(), port);
//                    server.send(sendPacket);
                }
            } else {
                String sender = UdpConnManagement.getUserByConn(ipAdr);
                UdpConnManagement.IPInfo ipInfo = UdpConnManagement.getPartnerIpInfo(sender);
                if(ipInfo != null){
                    DatagramPacket sendPacket = new DatagramPacket(recvData, recvData.length, ipInfo.host, ipInfo.port);
                    server.send(sendPacket);
                    String partnerAdr = UdpConnManagement.createAddr(ipInfo.host.getHostAddress(), ipInfo.port);
                    LOGGER.info("{}: ({}) forward to ({})", ipAdr, sender, partnerAdr);
                } else {
                    LOGGER.error("{}: ({}) not found partner", ipAdr, sender);
                }
            }
        }
    }
}
