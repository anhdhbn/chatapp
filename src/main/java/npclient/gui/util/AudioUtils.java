package npclient.gui.util;

import javax.sound.sampled.*;

public class AudioUtils {

    public static AudioFormat getAudioFormat(){
        float sampleRate = 8000.0F;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public static boolean isVoiceChatSupported() {
        AudioFormat format = AudioUtils.getAudioFormat();

        DataLine.Info inputInfo = new DataLine.Info(TargetDataLine.class, format);
        DataLine.Info outPutInfo = new DataLine.Info(SourceDataLine.class, format);

        return AudioSystem.isLineSupported(inputInfo) && AudioSystem.isLineSupported(outPutInfo);
    }
}
