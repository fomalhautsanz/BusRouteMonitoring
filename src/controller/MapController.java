package controller;

import javafx.animation.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.*;

public class MapController {

    private Bus bus;
    private Circle busNode;

    public StackPane createMapView() {

        // 🗺 MAP IMAGE
        ImageView mapView = new ImageView(
                new Image(getClass().getResource("/images/map.png").toExternalForm())
        );
        mapView.setFitWidth(900);
        mapView.setFitHeight(600);

        // 🧭 ROUTE SETUP
        Route route = new Route(null, null);
        route.getWaypoints().add(new Waypoint(100, 200));
        route.getWaypoints().add(new Waypoint(250, 220));
        route.getWaypoints().add(new Waypoint(450, 350));
        route.getWaypoints().add(new Waypoint(700, 400));

        // 🚍 BUS
        //bus = new Bus(route, 2.0);

        // 🔴 BUS NODE
        busNode = new Circle(6, Color.RED);
        busNode.setTranslateX(bus.getX());
        busNode.setTranslateY(bus.getY());

        StackPane mapPane = new StackPane(mapView, busNode);

        startAnimation();

        return mapPane;
    }

    // 🎬 MOVEMENT LOOP
    private void startAnimation() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(30), e -> {
                    bus.updatePosition();
                    busNode.setTranslateX(bus.getX());
                    busNode.setTranslateY(bus.getY());
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}

