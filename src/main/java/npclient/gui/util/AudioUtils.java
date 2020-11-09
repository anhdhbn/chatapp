package npclient.gui.util;

import javax.sound.sampled.*;

public class AudioUtils {

    private static final float RATE = 8000.0F;
    private static final int BITS_SIZE = 16;
    private static final int CHANNELS = 2;
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;

    public static AudioFormat getAudioFormat(){
        return new AudioFormat(RATE, BITS_SIZE, CHANNELS, SIGNED, BIG_ENDIAN);
    }

    public static boolean isVoiceChatSupported() {
        AudioFormat format = AudioUtils.getAudioFormat();

        DataLine.Info inputInfo = new DataLine.Info(TargetDataLine.class, format);
        DataLine.Info outPutInfo = new DataLine.Info(SourceDataLine.class, format);

        return AudioSystem.isLineSupported(inputInfo) && AudioSystem.isLineSupported(outPutInfo);
    }
}
