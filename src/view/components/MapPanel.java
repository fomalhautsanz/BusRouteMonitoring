package view.components;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class MapPanel extends Pane {

    private ImageView mapView;
    private Group mapGroup;

    private double zoomFactor = 1.0;
    private static final double MIN_ZOOM = 1.0;
    private static final double MAX_ZOOM = 5.0;

    public MapPanel(String imagePath) {
        setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        setMinSize(0, 0);

        Image mapImage = new Image(imagePath);
        mapView = new ImageView(mapImage);
        mapView.setPreserveRatio(true);

        mapGroup = new Group(mapView);
        getChildren().add(mapGroup);

        enableZoom();
        enableDrag();
        enableCoordinateLogging();
    }


    private void enableZoom() {
        setOnScroll((ScrollEvent event) -> {
            double delta = 1.2;

            if (event.getDeltaY() < 0) {
                zoomFactor /= delta;
            } else {
                zoomFactor *= delta;
            }

            zoomFactor = clamp(zoomFactor, MIN_ZOOM, MAX_ZOOM);

            Point2D mouseBefore = mapGroup.sceneToLocal(
                    event.getSceneX(), event.getSceneY()
            );

            mapGroup.setScaleX(zoomFactor);
            mapGroup.setScaleY(zoomFactor);

            Point2D mouseAfter = mapGroup.localToScene(mouseBefore);

            mapGroup.setTranslateX(
                    mapGroup.getTranslateX() + event.getSceneX() - mouseAfter.getX()
            );
            mapGroup.setTranslateY(
                    mapGroup.getTranslateY() + event.getSceneY() - mouseAfter.getY()
            );

            constrainMapPosition();
            event.consume();
        });
    }


    private void enableDrag() {
        final Point2D[] lastMouse = new Point2D[1];

        setOnMousePressed(e -> {
            lastMouse[0] = new Point2D(e.getSceneX(), e.getSceneY());
            e.consume();
        });

        setOnMouseDragged(e -> {
            double dx = e.getSceneX() - lastMouse[0].getX();
            double dy = e.getSceneY() - lastMouse[0].getY();

            mapGroup.setTranslateX(mapGroup.getTranslateX() + dx);
            mapGroup.setTranslateY(mapGroup.getTranslateY() + dy);

            lastMouse[0] = new Point2D(e.getSceneX(), e.getSceneY());

            constrainMapPosition();
            e.consume();
        });
    }



    private void constrainMapPosition() {
        Bounds bounds = mapGroup.getBoundsInParent();

        double paneWidth = getWidth();
        double paneHeight = getHeight();

        double dx = 0;
        double dy = 0;

        if (bounds.getWidth() <= paneWidth) {
            dx = paneWidth / 2 - (bounds.getMinX() + bounds.getWidth() / 2);
        } else {
            if (bounds.getMinX() > 0) {
                dx = -bounds.getMinX();
            } else if (bounds.getMaxX() < paneWidth) {
                dx = paneWidth - bounds.getMaxX();
            }
        }

        // Vertical constraint
        if (bounds.getHeight() <= paneHeight) {
            dy = paneHeight / 2 - (bounds.getMinY() + bounds.getHeight() / 2);
        } else {
            if (bounds.getMinY() > 0) {
                dy = -bounds.getMinY();
            } else if (bounds.getMaxY() < paneHeight) {
                dy = paneHeight - bounds.getMaxY();
            }
        }

        mapGroup.setTranslateX(mapGroup.getTranslateX() + dx);
        mapGroup.setTranslateY(mapGroup.getTranslateY() + dy);
    }


    public void addNodeToMap(Node node) {
        mapGroup.getChildren().add(node);
    }

    public void removeNodeFromMap(Node node) {
        mapGroup.getChildren().remove(node);
    }

    public ImageView getMapView() {
        return mapView;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void addMarker(MapMarker marker) {
        double x = marker.getTerminal().getX();
        double y = marker.getTerminal().getY();

        marker.setLayoutX(x);
        marker.setLayoutY(y);

        mapGroup.getChildren().add(marker);
    }

    public void addMarker(ImageView marker, double x, double y) {
        marker.setLayoutX(x);
        marker.setLayoutY(y);

        marker.setOnMouseClicked(e -> {
            System.out.println("Marker clicked at " + x + ", " + y);
        });

        mapGroup.getChildren().add(marker);
    }



    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private void enableCoordinateLogging() {
        mapGroup.setOnMouseClicked(e -> {

            // Convert mouse position to map coordinates (ignores zoom & pan)
            Point2D mapPoint = mapGroup.sceneToLocal(
                    e.getSceneX(),
                    e.getSceneY()
            );

            System.out.printf(
                    "Map X: %.2f, Map Y: %.2f%n",
                    mapPoint.getX(),
                    mapPoint.getY()
            );
        });
    }

    public void bringMarkersToFront() {
        for (Node n : new java.util.ArrayList<>(mapGroup.getChildren())) {
            if (n instanceof MapMarker) {
                n.toFront();
            }
        }
    }
}
