package view.components;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import model.Terminal;

public class MapPanel extends Pane {

    private ImageView mapView;
    private double zoomFactor = 1.0;
    private final double MIN_ZOOM = 1.0;
    private final double MAX_ZOOM = 5.0;
    private Group mapGroup;

    public MapPanel(String imagePath) {
        Image mapImage = new Image(imagePath);
        mapView = new ImageView(mapImage);

        mapView.setPreserveRatio(true);
        mapView.setFitWidth(mapImage.getWidth());
        mapView.setFitHeight(mapImage.getHeight());

        mapGroup = new Group(mapView);
        getChildren().add(mapGroup);

        setOnScroll((ScrollEvent event) -> {
            double delta = 1.2;
            double oldZoom = zoomFactor;

            if (event.getDeltaY() < 0) zoomFactor /= delta;
            else zoomFactor *= delta;

            if (zoomFactor < MIN_ZOOM) zoomFactor = MIN_ZOOM;
            if (zoomFactor > MAX_ZOOM) zoomFactor = MAX_ZOOM;

            // Compute mouse location in mapGroup local coordinates BEFORE scaling
            Point2D mouseLocalBefore = mapGroup.sceneToLocal(event.getSceneX(), event.getSceneY());

            // Apply new scale
            mapGroup.setScaleX(zoomFactor);
            mapGroup.setScaleY(zoomFactor);

            // After scaling, compute where that same local point is in scene coordinates
            Point2D mouseLocalSceneAfter = mapGroup.localToScene(mouseLocalBefore);

            // Calculate offset to move mapGroup so that the point under the cursor remains the same
            double dx = event.getSceneX() - mouseLocalSceneAfter.getX();
            double dy = event.getSceneY() - mouseLocalSceneAfter.getY();

            mapGroup.setLayoutX(mapGroup.getLayoutX() + dx);
            mapGroup.setLayoutY(mapGroup.getLayoutY() + dy);

            constrainMapPosition();
            event.consume();
        });

        this.setOnMouseClicked(e -> {
            // Convert click coordinates to original map coordinates (unzoomed)
            // Use sceneToLocal to map the scene coordinates to mapGroup's local coords
            Point2D local = mapGroup.sceneToLocal(e.getSceneX(), e.getSceneY());
            double originalX = local.getX();
            double originalY = local.getY();

            System.out.println("Clicked map at (original coordinates): " + originalX + ", " + originalY);
        });

        enableDrag();
    }

    public void addMarker(MapMarker marker) {
        double x = marker.getTerminal().getX(mapView.getImage().getWidth());
        double y = marker.getTerminal().getY(mapView.getImage().getHeight());

        marker.setLayoutX(x);
        marker.setLayoutY(y);

        mapGroup.getChildren().add(marker);
    }

    public void addTerminalMarker(Node marker, Terminal t) {
        double imgW = mapView.getImage().getWidth();
        double imgH = mapView.getImage().getHeight();
        double x = t.getX(imgW);
        double y = t.getY(imgH);
        //addMarker(marker, x, y);
    }

    private void enableDrag() {
        final double[] dragDelta = new double[2];

        mapGroup.setOnMousePressed(e -> {
            dragDelta[0] = e.getSceneX() - mapGroup.getLayoutX();
            dragDelta[1] = e.getSceneY() - mapGroup.getLayoutY();
        });

        mapGroup.setOnMouseDragged(e -> {
            double newX = e.getSceneX() - dragDelta[0];
            double newY = e.getSceneY() - dragDelta[1];

            mapGroup.setLayoutX(newX);
            mapGroup.setLayoutY(newY);

            constrainMapPosition();
        });
    }

    private void constrainMapPosition() {
        double scaledWidth = mapGroup.getBoundsInParent().getWidth();
        double scaledHeight = mapGroup.getBoundsInParent().getHeight();

        double newX = mapGroup.getLayoutX();
        double newY = mapGroup.getLayoutY();

        if (scaledWidth <= getWidth()) {
            newX = (getWidth() - scaledWidth) / 2;
        } else {
            double minX = getWidth() - scaledWidth;
            double maxX = 0;
            if (newX < minX) newX = minX;
            if (newX > maxX) newX = maxX;
        }

        if (scaledHeight <= getHeight()) {
            newY = (getHeight() - scaledHeight) / 2;
        } else {
            double minY = getHeight() - scaledHeight;
            double maxY = 0;
            if (newY < minY) newY = minY;
            if (newY > maxY) newY = maxY;
        }

        mapGroup.setLayoutX(newX);
        mapGroup.setLayoutY(newY);
    }

    public ImageView getMapView() {
        return mapView;
    }

    public void zoomIn() {
        double oldZoom = zoomFactor;
        zoomFactor *= 1.2;
        if (zoomFactor > MAX_ZOOM) zoomFactor = MAX_ZOOM;

        // Zoom towards center
        double f = (zoomFactor / oldZoom) - 1;
        double centerX = getWidth() / 2 - (mapGroup.getBoundsInParent().getMinX() + mapGroup.getBoundsInParent().getWidth() / 2);
        double centerY = getHeight() / 2 - (mapGroup.getBoundsInParent().getMinY() + mapGroup.getBoundsInParent().getHeight() / 2);

        mapGroup.setScaleX(zoomFactor);
        mapGroup.setScaleY(zoomFactor);

        mapGroup.setLayoutX(mapGroup.getLayoutX() - f * centerX);
        mapGroup.setLayoutY(mapGroup.getLayoutY() - f * centerY);

        constrainMapPosition();
    }

    public void zoomOut() {
        double oldZoom = zoomFactor;
        zoomFactor /= 1.2;
        if (zoomFactor < MIN_ZOOM) zoomFactor = MIN_ZOOM;

        // Zoom towards center
        double f = (zoomFactor / oldZoom) - 1;
        double centerX = getWidth() / 2 - (mapGroup.getBoundsInParent().getMinX() + mapGroup.getBoundsInParent().getWidth() / 2);
        double centerY = getHeight() / 2 - (mapGroup.getBoundsInParent().getMinY() + mapGroup.getBoundsInParent().getHeight() / 2);

        mapGroup.setScaleX(zoomFactor);
        mapGroup.setScaleY(zoomFactor);

        mapGroup.setLayoutX(mapGroup.getLayoutX() - f * centerX);
        mapGroup.setLayoutY(mapGroup.getLayoutY() - f * centerY);

        constrainMapPosition();
    }

    public void addMarker(ImageView marker, double x, double y) {
        marker.setLayoutX(x);
        marker.setLayoutY(y);

        marker.setOnMouseClicked(e -> {
            System.out.println("Marker clicked at " + x + ", " + y);
        });

        mapGroup.getChildren().add(marker);
    }

    // New helpers to add/remove arbitrary nodes into the scaled mapGroup
    public void addNodeToMap(Node node) {
        mapGroup.getChildren().add(node);
    }

    public void removeNodeFromMap(Node node) {
        mapGroup.getChildren().remove(node);
    }

    public double getZoomFactor() {
        return zoomFactor;
    }
}