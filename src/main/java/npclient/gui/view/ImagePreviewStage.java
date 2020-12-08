package npclient.gui.view;

import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import npclient.gui.manager.StageManager;

public class ImagePreviewStage extends Stage {

    private OnDownloadListener listener;

    public ImagePreviewStage(Image image) {
        ImagePreview pane = new ImagePreview();
        pane.setImage(image);

        Rectangle2D screen = Screen.getPrimary().getBounds();
        Scene scene = new Scene(pane, screen.getWidth(), screen.getHeight());

        setScene(scene);
        initOwner(StageManager.getInstance().getPrimaryStage());
        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.S && e.isShortcutDown()) {
                    if (listener != null)
                        listener.onDownload();
                } else if (e.getCode() == KeyCode.ESCAPE) {
                    close();
                }
            }
        });
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        this.listener = listener;
    }

    public interface OnDownloadListener {
        void onDownload();
    }
}
