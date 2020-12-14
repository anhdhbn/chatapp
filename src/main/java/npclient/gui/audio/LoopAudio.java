package npclient.gui.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

public abstract class LoopAudio extends Thread {

    private MediaPlayer mediaPlayer;

    private final String path;

    public LoopAudio(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        URL resource = getClass().getResource(path);
        mediaPlayer = new MediaPlayer(new Media(resource.toString()));
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();
        super.run();
    }

    public void stopMedia() {
        mediaPlayer.stop();
    }
}
