package npclient.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import npclient.CliLogger;
import npclient.MyAccount;
import npclient.core.UDPConnection;
import npclient.core.command.Publisher;
import npclient.core.command.UDPRegister;
import npclient.core.command.VoiceListener;
import npclient.core.command.VoiceSpeaker;
import npclient.gui.util.AudioUtils;
import npclient.gui.util.UIUtils;
import nputils.Constants;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
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

            logger.debug("Start audio output");
            SourceDataLine audioOutput = AudioSystem.getSourceDataLine(format);
            audioOutput.open(format);
            increaseVolume(audioOutput);
            audioOutput.start();

            logger.debug("Start audio input");
            TargetDataLine audioInput = AudioSystem.getTargetDataLine(format);
            audioInput.open(format);
            increaseVolume(audioInput);
            audioInput.start();

            final UDPConnection udpConn = MyAccount.getInstance().getUdpConn();
            final String name = MyAccount.getInstance().getName();

            logger.debug("Register UDP Connection to the server");
            new UDPRegister()
                    .setName(name)
                    .setConnection(udpConn)
                    .register();

            logger.debug("Start voice listener");
            listener = new VoiceListener()
                    .setConnection(udpConn)
                    .setAudioInput(audioInput);
            listener.post();

            logger.debug("Start voice speaker");
            speaker = new VoiceSpeaker()
                    .setConnection(udpConn)
                    .setAudioOutput(audioOutput);
            speaker.listen();

        } catch (LineUnavailableException | IOException e) {
            logger.error("System not support voice chat: " + e.getMessage());
            UIUtils.showErrorAlert("System not support voice chat: " + e.getMessage());
        }
    }

    private void increaseVolume(DataLine dataLine) {
        if (dataLine.isControlSupported(FloatControl.Type.VOLUME)) {
            FloatControl volume = (FloatControl) dataLine.getControl(FloatControl.Type.VOLUME);
            volume.setValue(volume.getMaximum());
        }
    }

    public void setUser1(String user1) {
        this.lUser1.setText(user1);
    }

    public void setUser2(String user2) {
        this.lUser2.setText(user2);
    }

    public void stop() {
        logger.debug("Cancel listener & speaker");
        listener.cancel();
        speaker.cancel();

        logger.debug("Send Voice Quit signal to the partner");
        final String topic = "voice/" + lUser2.getText();
        final String username = lUser1.getText();

        new Publisher(topic, username)
                .putData(Constants.VOICE_QUIT)
                .post();
    }
}
