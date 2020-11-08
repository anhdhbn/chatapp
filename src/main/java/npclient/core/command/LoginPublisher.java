package npclient.core.command;

import javafx.application.Platform;
import npclient.core.TCPConnection;
import npclient.core.UDPConnection;
import npclient.exception.ExistUserException;
import nputils.Constants;
import nputils.DataTransfer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

public class LoginPublisher extends TCPPublisher {

    public LoginPublisher(String username) {
        super("login/" + username, username);
        dataTransfer = new DataTransfer(topic, username, Constants.PUBLISH);
    }

    @Override
    protected void handlePublish(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws Exception {
        outputStream.writeObject(dataTransfer);

        logger.debug("Checking existing user");
        DataTransfer res = (DataTransfer) inputStream.readObject();
        boolean isLoginSuccess = (boolean) res.data;

        if (isLoginSuccess) {
            logger.debug("No user found. Initialize UDP Connection to server");
            dataTransfer.data = registerUDPConnection();

            if (successListener != null) {
                logger.debug("On Success Callback");
                try {
                    Platform.runLater(() -> successListener.onReceive(dataTransfer));
                } catch (IllegalStateException ex) {
                    successListener.onReceive(dataTransfer);
                }
            }
        } else {
            logger.error("Found Existing User. Login Failure!");
            handleError(new ExistUserException());
        }
    }

    private UDPConnection registerUDPConnection() throws IOException {
        UDPConnection udpConn = new UDPConnection();

        byte[] nameBytes = dataTransfer.name.getBytes();
        byte[] initBuf = new byte[1024];
        initBuf[0] = (byte) dataTransfer.name.length();
        System.arraycopy(nameBytes, 0, initBuf, 1, nameBytes.length);

        DatagramPacket initPacket = new DatagramPacket(initBuf, initBuf.length,
                UDPConnection.getServInetAddr(),
                npclient.Constants.UDP_PORT
        );

        udpConn.send(initPacket);

        byte[] recvBuf =  new byte[1024];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
        udpConn.receive(recvPacket);

        logger.debug(new String(recvPacket.getData()));

        return udpConn;
    }
}
