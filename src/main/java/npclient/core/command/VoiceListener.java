package npclient.core.command;

import javafx.application.Platform;
import npclient.CliLogger;
import npclient.core.UDPConnection;

import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class VoiceListener extends AbstractPublisher {

    private static final CliLogger logger = CliLogger.get(VoiceListener.class);

    private TargetDataLine audioInput;

    private UDPConnection connection;

    public VoiceListener(String username) {
        super(username);
    }

    @Override
    public void post() {
        String threadName = "Voice Publisher Thread";
        new Thread(this, threadName).start();
    }

    public VoiceListener setAudioInput(TargetDataLine audioInput) {
        this.audioInput = audioInput;
        return this;
    }

    public VoiceListener setConnection(UDPConnection connection) {
        this.connection = connection;
        return this;
    }

    @Override
    public void run() {
        InetAddress inetAddr = connection.getInetAddress();
        int port = connection.getPort();

        byte[] buffer = new byte[512];
        long pack = 0L;

        while (!isCancel) {
            try {
                int read = audioInput.read(buffer, 0, buffer.length);
                logger.debug("Read " + read + " bytes from audio input");

                DatagramPacket data = new DatagramPacket(buffer, buffer.length, inetAddr, port);
                connection.send(data);

                logger.debug("Send packet #" + pack);

            } catch (IOException ex) {
                logger.error(ex.getMessage());
                if (errorListener != null)
                    errorListener.onReceive(ex);
            }
        }

        logger.debug("Recorder is stop");
        audioInput.drain();
        audioInput.close();

        logger.debug("Audio is drain and close");
    }
}
