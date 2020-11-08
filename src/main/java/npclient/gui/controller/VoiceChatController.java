package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import npclient.CliConstants;
import npclient.CliLogger;
import npclient.MyAccount;
import npclient.core.UDPConnection;
import npclient.core.command.Publisher;
import npclient.core.command.VoiceListener;
import npclient.core.command.VoiceSpeaker;
import npclient.gui.util.AudioUtils;
import nputils.Constants;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class VoiceChatController implements Initializable {

    private static final CliLogger logger = CliLogger.get(VoiceChatController.class);

    @FXML
    private Label lUser1;
    @FXML
    private Label lUser2;

    private VoiceListener listener;
    private VoiceSpeaker speaker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            AudioFormat format = AudioUtils.getAudioFormat();

            SourceDataLine audioOutput = AudioSystem.getSourceDataLine(format);

            audioOutput.open(format);
            audioOutput.start();

            TargetDataLine audioInput = AudioSystem.getTargetDataLine(format);
            audioInput.open(format);
            audioInput.start();

            final UDPConnection udpConn = MyAccount.getInstance().getUdpConn();

            String name = MyAccount.getInstance().getName();
            byte[] nameBytes = name.getBytes();
            byte[] initBuf = new byte[Constants.BUFFER_SIZE];
            initBuf[0] = 0;
            initBuf[1] = (byte) name.length();
            System.arraycopy(nameBytes, 0, initBuf, 2, nameBytes.length);

            DatagramPacket initPacket = new DatagramPacket(initBuf, initBuf.length,
                    UDPConnection.getServInetAddr(),
                    CliConstants.UDP_PORT
            );

            udpConn.send(initPacket);

            listener = new VoiceListener()
                    .setConnection(udpConn)
                    .setAudioInput(audioInput);
            listener.post();

            speaker = new VoiceSpeaker()
                    .setConnection(udpConn)
                    .setAudioOutput(audioOutput);
            speaker.listen();

        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser1(String user1) {
        this.lUser1.setText(user1);
    }

    public void setUser2(String user2) {
        this.lUser2.setText(user2);
    }

    public void stop() {
        listener.cancel();
        speaker.cancel();
        new Publisher("voice/" + lUser2.getText(), lUser1.getText())
                .putData(Constants.VOICE_QUIT)
                .post();
    }
}
