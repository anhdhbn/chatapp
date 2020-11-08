package npclient.core.command;

import javafx.application.Platform;
import npclient.CliLogger;
import npclient.core.UDPConnection;

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
            byte[] buffer = new byte[512];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            logger.debug("Waiting for incoming data...");

            while (!isCancel){
                connection.receive(incoming);
                buffer = incoming.getData();
                audioOutput.write(buffer, 0, buffer.length);
            }

            logger.debug("Speaker is stop");

            audioOutput.drain();
            audioOutput.close();

            logger.debug("Audio is drain and close");

        } catch (IOException ex) {
            logger.error(ex.getMessage());
            if (errorListener != null) {
                try {
                    Platform.runLater(() -> errorListener.onReceive(ex));
                } catch (IllegalStateException e) {
                    errorListener.onReceive(ex);
                }
            }
        }
    }


}
