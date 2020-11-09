package npserver.udpserver;

import npserver.ServerTest;
import npserver.handler.ReadWriteHandler;
import nputils.Constants;
import nputils.DataTransfer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;
import java.util.UUID;

public class VoiceCallTest extends ServerTest {
    public void clientCanCallEachOther() throws IOException, InterruptedException {
        this.generateClient(2);
        ReadWriteHandler handler1 = this.handlers.get(0);
        ReadWriteHandler handler2 = this.handlers.get(1);

        DatagramSocket udp1 = this.udpConns.get(0);
        DatagramSocket udp2 = this.udpConns.get(1);

        this.delay();
        String topic = Constants.PREFIX_VOICE + Constants.SPLITTER + handler2.name;
        DataTransfer dataReq = new DataTransfer(topic, handler1.name, Constants.PUBLISH, Constants.VOICE_REQUEST);
        handler1.sendObj(dataReq);


        handler2.receiveObj();
        topic = Constants.PREFIX_VOICE + Constants.SPLITTER + handler1.name;
        DataTransfer dataAc = new DataTransfer(topic, handler2.name, Constants.PUBLISH, Constants.VOICE_ACCEPT);
        handler2.sendObj(dataAc);

        // voice exchange
        Thread thread = new Thread(()->{
            while (true){
                try {
                    sendDataUdp(udp1);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });

        Thread thread2 = new Thread(()->{
            while (true){
                try {
                    recvDataUdp(udp1);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });

        Thread thread3 = new Thread(()->{
            while (true){
                try {
                    sendDataUdp(udp2);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });

        Thread thread4 = new Thread(()->{
            while (true){
                try {
                    recvDataUdp(udp2);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });

        thread.start();
        thread2.start();
        thread3.start();
        thread4.start();
        this.delay();
        udp1.close();
        udp2.close();
    }

    public void sendDataUdp(DatagramSocket udpConn) throws IOException {
        String data = UUID.randomUUID().toString();
        byte[] sendData = new byte[Constants.BUFFER_SIZE];
        System.arraycopy(data.getBytes(), 0, sendData, 0, data.length());
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), cr.portUdp);
        udpConn.send(sendPacket);
    }

    public void recvDataUdp(DatagramSocket udpConn) throws IOException {
        byte[] sendData = new byte[Constants.BUFFER_SIZE];
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
        udpConn.receive(sendPacket);
    }
}
