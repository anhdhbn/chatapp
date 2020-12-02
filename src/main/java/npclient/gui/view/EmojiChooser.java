package npclient.gui.view;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import npclient.CliLogger;
import npclient.gui.manager.StageManager;
import npclient.gui.task.RetrieveEmojiTask;
import npclient.gui.util.UIUtils;
import nputils.Emoji;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmojiChooser extends Stage {

    private static final CliLogger logger = CliLogger.get(EmojiChooser.class);

    private OnEmojiListener listener;

    public EmojiChooser() {
        super();
        setTitle("Emoji Chooser");

        Parent root = initializeView();
        Scene scene = new Scene(root);
        setScene(scene);

        scene.getStylesheets().add(getClass().getResource("/css/base.css").toString());

        setResizable(false);

        initOwner(StageManager.getInstance().getPrimaryStage());
    }

    public EmojiChooser setListener(OnEmojiListener listener) {
        this.listener = listener;
        return this;
    }

    private Parent initializeView() {
        GridPane grid = new GridPane();
        grid.setHgap(PADDING);
        grid.setVgap(PADDING);
        grid.setPadding(new Insets(PADDING));

        ExecutorService es = Executors.newFixedThreadPool(5);

        int row = 0, col = 0;
        for (Emoji emoji : Emoji.values()) {
            ProgressIndicator indicator = new ProgressIndicator();
            indicator.setMaxSize(INDICATOR_SIZE, INDICATOR_SIZE);
            grid.add(indicator, col, row);

            ImageView imageView = new ImageView();
            imageView.setFitHeight(EMOJI_SIZE);
            imageView.setFitWidth(EMOJI_SIZE);
            imageView.getStyleClass().add("emoji");

            grid.add(imageView, col, row);

            RetrieveEmojiTask task = new RetrieveEmojiTask(emoji);

            task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    Image image = task.getValue();
                    imageView.setImage(image);
                    grid.getChildren().remove(indicator);
                }
            });
            es.submit(task);

            GridPane.setHalignment(imageView, HPos.CENTER);
            GridPane.setValignment(imageView, VPos.CENTER);

            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    logger.debug("Choose " + emoji.name());
                    logger.debug(imageView.getStyleClass());
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
        scrollPane.setPrefViewportHeight(HEIGHT);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //remove event
        scrollPane.getStyleClass().add("emoji-popup");
        scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });

        return scrollPane;
    }

    public interface OnEmojiListener {
        void onSelect(Emoji emoji);
    }

    private static final int PADDING = 20;
    private static final int EMOJI_SIZE = 64;
    private static final double INDICATOR_SIZE = EMOJI_SIZE * 0.75f;
    private static final int COLUMN = 5;
    private static final int HEIGHT = 400;
}
