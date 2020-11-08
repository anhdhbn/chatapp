package npserver.udpserver;

import npserver.ServerTest;
import nputils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

public class UdpServerTest extends ServerTest {
    @Test
    public void clientPublishToUdpServer() throws IOException, InterruptedException {
        byte[] sendData = new byte[Constants.BUFFER_SIZE];
        byte[] sendRecv = new byte[Constants.BUFFER_SIZE];

        sendData[0] = (byte)this.user.length();
        System.arraycopy(this.user.getBytes(), 0, sendData, 1, this.user.length());
        DatagramSocket clientSocket = new DatagramSocket(6789);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), cr.portUdp);
        clientSocket.send(sendPacket);

        DatagramPacket recvPacket = new DatagramPacket(sendRecv, sendRecv.length);
        clientSocket.receive(recvPacket);

        int len = recvPacket.getData()[0];
        byte[] newBuff = new byte[len];
        System.arraycopy(recvPacket.getData(), 1, newBuff, 0, len);
        String result = new String(newBuff);
        Assertions.assertEquals(this.user, result);
    }
}
