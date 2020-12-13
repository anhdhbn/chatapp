package npclient.gui.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class NotiAudio extends Thread {
    @Override
    public void run() {
        Media hit = new Media(getClass().getResource("/audio/noti.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        super.run();
    }
}
