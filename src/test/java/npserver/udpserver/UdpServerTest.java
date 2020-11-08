package npserver.udpserver;

import npserver.ServerTest;
import nputils.Constants;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

public class UdpServerTest extends ServerTest {
    @Test
    public void clientPublishToUdpServer() throws IOException, InterruptedException {
        byte[] sendData = new byte[Constants.BUFFER_SIZE];

        System.arraycopy(this.user.getBytes(), 0, sendData, 0, this.user.length());
        DatagramSocket clientSocket = new DatagramSocket(6789);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), cr.portUdp);
        clientSocket.send(sendPacket);
    }
}
