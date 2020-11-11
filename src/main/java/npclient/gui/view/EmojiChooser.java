package npclient.gui.view;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import npclient.CliLogger;
import npclient.gui.manager.StageManager;
import npclient.gui.util.UIUtils;
import nputils.Emoji;

public class EmojiChooser extends Stage {

    private static final CliLogger logger = CliLogger.get(EmojiChooser.class);

    private OnEmojiListener listener;

    public EmojiChooser() {
        super();
        setTitle("Emoji Chooser");

        Parent root = initializeView();
        Scene scene = new Scene(root);
        setScene(scene);

        setResizable(false);

        initOwner(StageManager.getInstance().getPrimaryStage());
    }

    public EmojiChooser setListener(OnEmojiListener listener) {
        this.listener = listener;
        return this;
    }

    private Parent initializeView() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20, 20, 20, 20));

        int row = 0, col = 0;
        for (Emoji emoji : Emoji.values()) {
            Image image = UIUtils.Emoji.toImage(emoji);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(EMOJI_SIZE);
            imageView.setFitWidth(EMOJI_SIZE);

            grid.add(imageView, col, row);
            GridPane.setHalignment(imageView, HPos.CENTER);
            GridPane.setValignment(imageView, VPos.CENTER);

            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    logger.debug("Choose " + emoji.name());
                    if (listener != null) {
                        listener.onSelect(emoji);
                    }
                    close();
                }
            });

            col++;
            if (col == COLUMN) {
                row++;
                col = 0;
            }
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(HEIGH);

        return scrollPane;
    }

    public interface OnEmojiListener {
        void onSelect(Emoji emoji);
    }

    private static final int EMOJI_SIZE = 64;
    private static final int COLUMN = 5;
    private static final int HEIGH = 400;
}
