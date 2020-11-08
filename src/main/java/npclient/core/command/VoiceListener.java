package npclient.core.command;

import npclient.CliLogger;
import npclient.Constants;
import npclient.core.UDPConnection;

import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.net.DatagramPacket;

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
        byte[] buffer = new byte[1024];
        long pack = 0L;

        while (!isCancel) {
            try {
                int read = audioInput.read(buffer, 0, buffer.length);
                logger.debug("Read " + read + " bytes from audio input");

                DatagramPacket data = new DatagramPacket(buffer, buffer.length,
                        UDPConnection.getServInetAddr(),
                        Constants.UDP_PORT
                );
                connection.send(data);

                logger.debug("Send packet #" + pack);

            } catch (IOException ex) {
                logger.error("Failed to listen: " + ex.getMessage());
                handleError(ex);
            }
        }

        logger.debug("Recorder is stop");
        audioInput.drain();
        audioInput.close();

        logger.debug("Audio is drain and close");
    }
}
