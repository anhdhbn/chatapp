package npclient.core.command;

import javafx.application.Platform;
import npclient.CliConstants;
import npclient.core.UDPConnection;
import npclient.exception.ExistUserException;
import npclient.exception.InvalidNameException;
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
        new UDPRegister()
                .setName(dataTransfer.name)
                .setConnection(udpConn)
                .register();
        return udpConn;
    }

    @Override
    protected void preprocess(DataTransfer message) throws Exception {
        if (!isNameValid(message.name))
            throw new InvalidNameException(message.name);
    }

    private boolean isNameValid(String name) {
        return name.matches("[a-zA-Z0-9_\\s]+") && !name.isEmpty();
    }
}
