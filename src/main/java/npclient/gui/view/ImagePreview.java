package npclient.gui.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;
import npclient.gui.controller.ImagePreviewController;

import java.io.IOException;

public class ImagePreview extends StackPane {

    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0d);
    private final DoubleProperty deltaY = new SimpleDoubleProperty(0.0d);

    private final PanAndZoomPane panAndZoomPane;

    public ImagePreview() {
        // black background
        Rectangle2D screen = Screen.getPrimary().getBounds();
        Rectangle rect = new Rectangle(screen.getWidth(), screen.getHeight(), Color.BLACK);
        getChildren().add(rect);

        // create canvas
        panAndZoomPane = new PanAndZoomPane();
        zoomProperty.bind(panAndZoomPane.myScaleProperty());
        deltaY.bind(panAndZoomPane.deltaYProperty());
        getChildren().add(panAndZoomPane);
        panAndZoomPane.toFront();

        SceneGestures sceneGestures = new SceneGestures(panAndZoomPane);
        addEventFilter(MouseEvent.MOUSE_CLICKED, sceneGestures.getOnMouseClickedEventHandler());
        addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }

    public void setImage(Image image) {
        ImageView imageView = new ImageView(image);
        normalize(imageView);
        panAndZoomPane.getChildren().add(imageView);
        StackPane.setAlignment(imageView, Pos.CENTER);
    }

    private void normalize(ImageView imageView) {
        Image image = imageView.getImage();
        final double imageRatio = image.getWidth() / image.getHeight();
        Rectangle2D screen = Screen.getPrimary().getBounds();
        final double screenRatio = screen.getWidth() / screen.getHeight();
        double width, height;
        if (imageRatio > screenRatio) {
            width = screen.getWidth();
            height = screen.getWidth() / imageRatio;
        } else if (imageRatio < screenRatio) {
            height = screen.getHeight();
            width = screen.getHeight() * imageRatio;
        } else {
            width = screen.getWidth();
            height = screen.getHeight();
        }
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }

    private static class PanAndZoomPane extends StackPane {

        public static final double DEFAULT_DELTA = 1.3d;
        private final DoubleProperty myScale = new SimpleDoubleProperty(1.0);
        private final DoubleProperty deltaY = new SimpleDoubleProperty(0.0);
        private final Timeline timeline;

        public PanAndZoomPane() {
            this.timeline = new Timeline(60);

            // add scale transform
            scaleXProperty().bind(myScale);
            scaleYProperty().bind(myScale);
        }

        public DoubleProperty deltaYProperty() {
            return deltaY;
        }

        public DoubleProperty myScaleProperty() {
            return myScale;
        }

        public double getScale() {
            return myScale.get();
        }

        public void setScale(double scale) {
            myScale.set(scale);
        }

        public void setPivot(double x, double y, double scale) {
            // note: pivot value must be untransformed, i. e. without scaling
            // timeline that scales and moves the node
            if (scale >= 1) {
                timeline.getKeyFrames().clear();
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.millis(200), new KeyValue(translateXProperty(), getTranslateX() - x)),
                        new KeyFrame(Duration.millis(200), new KeyValue(translateYProperty(), getTranslateY() - y)),
                        new KeyFrame(Duration.millis(200), new KeyValue(myScale, scale))
                );
                System.out.println("x=" + x + ",y=" + y + ",scale=" + scale);
                timeline.play();
            }
        }

        public void fitWidth() {
            double scale = getParent().getLayoutBounds().getMaxX() / getLayoutBounds().getMaxX();
            double oldScale = getScale();

            double f = scale - oldScale;

            double dx = getTranslateX() - getBoundsInParent().getMinX() - getBoundsInParent().getWidth() / 2;
            double dy = getTranslateY() - getBoundsInParent().getMinY() - getBoundsInParent().getHeight() / 2;

            double newX = f * dx + getBoundsInParent().getMinX();
            double newY = f * dy + getBoundsInParent().getMinY();

            setPivot(newX, newY, scale);
        }

        public void resetZoom() {
            double scale = 1.0d;

            double x = getTranslateX();
            double y = getTranslateY();

            setPivot(x, y, scale);
        }

        public double getDeltaY() {
            return deltaY.get();
        }

        public void setDeltaY(double dY) {
            deltaY.set(dY);
        }
    }

    /**
     * Mouse drag context used for scene and nodes.
     */
    private static class DragContext {
        double mouseAnchorX;
        double mouseAnchorY;

        double translateAnchorX;
        double translateAnchorY;
    }

    /**
     * Listeners for making the scene's canvas draggable and zoomable
     */
    public static class SceneGestures {

        private final DragContext sceneDragContext = new DragContext();
        private final PanAndZoomPane panAndZoomPane;

        private final EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                sceneDragContext.mouseAnchorX = event.getX();
                sceneDragContext.mouseAnchorY = event.getY();

                sceneDragContext.translateAnchorX = panAndZoomPane.getTranslateX();
                sceneDragContext.translateAnchorY = panAndZoomPane.getTranslateY();

            }

        };
        private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                panAndZoomPane.setTranslateX(sceneDragContext.translateAnchorX + event.getX() - sceneDragContext.mouseAnchorX);
                panAndZoomPane.setTranslateY(sceneDragContext.translateAnchorY + event.getY() - sceneDragContext.mouseAnchorY);
                System.out.println("x="+panAndZoomPane.getTranslateX()+",y="+panAndZoomPane.getTranslateY());
                event.consume();
            }
        };
        /**
         * Mouse wheel handler: zoom to pivot point
         */
        private final EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double delta = PanAndZoomPane.DEFAULT_DELTA;

                double scale = panAndZoomPane.getScale(); // currently we only use Y, same value is used for X
                double oldScale = scale;

                panAndZoomPane.setDeltaY(event.getDeltaY());
                if (panAndZoomPane.deltaY.get() < 0) {
                    scale /= delta;
                } else {
                    scale *= delta;
                }

                double f = (scale / oldScale) - 1;
                double dx = (event.getX() - (panAndZoomPane.getBoundsInParent().getWidth() / 2 + panAndZoomPane.getBoundsInParent().getMinX()));
                double dy = (event.getY() - (panAndZoomPane.getBoundsInParent().getHeight() / 2 + panAndZoomPane.getBoundsInParent().getMinY()));

                panAndZoomPane.setPivot(f * dx, f * dy, scale);

                event.consume();
            }
        };
        /**
         * Mouse click handler
         */
        private final EventHandler<MouseEvent> onMouseClickedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        panAndZoomPane.resetZoom();
                    }
                }
                if (event.getButton().equals(MouseButton.SECONDARY)) {
                    if (event.getClickCount() == 2) {
                        panAndZoomPane.fitWidth();
                    }
                }
            }
        };

        public SceneGestures(PanAndZoomPane canvas) {
            this.panAndZoomPane = canvas;
        }

        public EventHandler<MouseEvent> getOnMouseClickedEventHandler() {
            return onMouseClickedEventHandler;
        }

        public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
            return onMousePressedEventHandler;
        }

        public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
            return onMouseDraggedEventHandler;
        }

        public EventHandler<ScrollEvent> getOnScrollEventHandler() {
            return onScrollEventHandler;
        }
    }
}
