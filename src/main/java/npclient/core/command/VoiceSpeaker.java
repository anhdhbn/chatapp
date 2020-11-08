package npclient.core.command;

import npclient.CliLogger;
import npclient.core.UDPConnection;
import nputils.Constants;

import javax.sound.sampled.SourceDataLine;
import java.io.IOException;
import java.net.DatagramPacket;

public class VoiceSpeaker extends AbstractTask {

    private static final CliLogger logger = CliLogger.get(VoiceSpeaker.class);

    private SourceDataLine audioOutput;

    private UDPConnection connection;

    public VoiceSpeaker setAudioOutput(SourceDataLine audioOutput) {
        this.audioOutput = audioOutput;
        return this;
    }

    public VoiceSpeaker setConnection(UDPConnection connection) {
        this.connection = connection;
        return this;
    }

    @Override
    public void run(){
        try {
            byte[] buffer = new byte[Constants.BUFFER_SIZE];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            logger.debug("Waiting for incoming data...");

            while (!isCancel){
                connection.receive(incoming);
                buffer = incoming.getData();
                logger.debug("Receive " + buffer.length + " bytes");
                audioOutput.write(buffer, 1, buffer.length);
            }

            logger.debug("Speaker is stop");

            audioOutput.drain();
            audioOutput.close();

            logger.debug("Audio is drain and close");

        } catch (IOException e) {
            logger.error("Failed to subscribe: " + e.getMessage());
            handleError(e);
        }
    }

    public void listen() {
        String threadName = "Voice Speaker Thread";
        new Thread(this, threadName).start();
    }
}
