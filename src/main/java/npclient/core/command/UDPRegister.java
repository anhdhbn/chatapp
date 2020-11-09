package npclient.core.command;

import npclient.CliConstants;
import npclient.core.UDPConnection;
import nputils.Constants;

import java.io.IOException;
import java.net.DatagramPacket;

public class UDPRegister {

    private UDPConnection connection;
    private String name;

    public UDPRegister setConnection(UDPConnection conn) {
        this.connection = conn;
        return this;
    }

    public UDPRegister setName(String name) {
        this.name = name;
        return this;
    }

    public void register() throws IOException {
        byte[] nameBytes = name.getBytes();
        byte[] initBuf = new byte[Constants.BUFFER_SIZE];
        initBuf[0] = 0;
        initBuf[1] = (byte) name.length();
        System.arraycopy(nameBytes, 0, initBuf, 2, nameBytes.length);

        DatagramPacket initPacket = new DatagramPacket(initBuf, initBuf.length,
                UDPConnection.getServInetAddr(),
                CliConstants.UDP_PORT
        );

        connection.send(initPacket);
    }
}
