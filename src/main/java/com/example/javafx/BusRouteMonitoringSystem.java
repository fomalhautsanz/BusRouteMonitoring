package com.example.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class BusRouteMonitoringSystem extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main container
        HBox mainContainer = new HBox();
        mainContainer.setStyle("-fx-background-color: #f0f0f0;");

        // Left Panel
        VBox leftPanel = createLeftPanel();

        // Right Panel (Map)
        Pane mapPanel = createMapPanel();

        mainContainer.getChildren().addAll(leftPanel, mapPanel);
        HBox.setHgrow(mapPanel, Priority.ALWAYS);

        Scene scene = new Scene(mainContainer, 1200, 700);
        primaryStage.setTitle("Bus Route Monitoring System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLeftPanel() {

        // ---------- MAIN CONTAINER ----------
        BorderPane container = new BorderPane();
        container.setPrefWidth(500);
        container.setPrefHeight(900);
        container.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 0 2 0 0;");
        container.setPadding(new Insets(20, 30, 20, 30));

        // ---------- TOP AREA (Buttons) ----------
        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 22px; -fx-cursor: hand;");

        Button zoomOutBtn = new Button("—");
        zoomOutBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 22px; -fx-cursor: hand;");

        HBox topButtons = new HBox(15, closeBtn, zoomOutBtn);
        topButtons.setAlignment(Pos.TOP_LEFT);

        VBox topArea = new VBox(20, topButtons);
        topArea.setAlignment(Pos.TOP_LEFT);

        // ---------- LOGO AREA (Manual Positioning) ----------
        Pane logoPane = new Pane();
        logoPane.setPrefHeight(180);

        ImageView imageView = new ImageView(
                new Image("file:C:/Users/johar/IdeaProjects/JavaFx/src/main/java/com/example/javafx/bus logo.png")
        );
        imageView.setFitWidth(140);
        imageView.setPreserveRatio(true);

        // MANUAL POSITION (editable)
        imageView.setLayoutX(200);
        imageView.setLayoutY(10);

        Label titleLabel = new Label("BUS ROUTE\nMONITORING SYSTEM");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1a5490"));
        titleLabel.setAlignment(Pos.CENTER);

        // MANUAL POSITION FOR TITLE
        titleLabel.setLayoutX(40);   // <-- adjust this to center better
        titleLabel.setLayoutY(100);

        logoPane.getChildren().addAll(imageView, titleLabel);

        // ---------- START TERMINAL ----------
        Label startLabel = new Label("START TERMINAL");
        startLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startLabel.setTextFill(Color.web("#1a5490"));

        ComboBox<String> startDropdown = new ComboBox<>();
        startDropdown.setPrefWidth(300);
        startDropdown.setPrefHeight(40);

        startDropdown.getItems().addAll(
                "Tagum City, Davao Del Norte",
                "Davao City, Davao Del Sur",
                "Panabo City, Davao Del Norte",
                "Mati City, Davao Oriental",
                "Kidapawan City, Cotabato",
                "Digos City, Davao del Sur"
        );

// Default selection
        startDropdown.setValue("Digos City, Davao del Sur");

        startDropdown.setStyle(
                "-fx-border-color: #1a5490; -fx-border-width: 2; -fx-background-radius: 5; " +
                        "-fx-border-radius: 5; -fx-font-size: 14px;"
        );

        VBox startSection = new VBox(10, startLabel, startDropdown);
        startSection.setAlignment(Pos.CENTER);

        // ---------- DESTINATION ----------
        Label destLabel = new Label("DESTINATION");
        destLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        destLabel.setTextFill(Color.web("#1a5490"));

        ComboBox<String> destinationDropdown = new ComboBox<>();
        destinationDropdown.setPrefWidth(300);
        destinationDropdown.setPrefHeight(40);

        destinationDropdown.getItems().addAll(
                "Tagum City, Davao Del Norte",
                "Davao City, Davao Del Sur",
                "Panabo City, Davao Del Norte",
                "Mati City, Davao Oriental",
                "Kidapawan City, Cotabato",
                "Digos City, Davao del Sur"
        );


        destinationDropdown.setValue("Tagum City, Davao Del Norte");


        destinationDropdown.setStyle(
                "-fx-border-color: #1a5490; -fx-border-width: 2; -fx-background-radius: 5; " +
                        "-fx-border-radius: 5; -fx-font-size: 14px;"
        );


        VBox destSection = new VBox(10, destLabel, destinationDropdown);
        destSection.setAlignment(Pos.CENTER);

        // ---------- CENTER STACK ----------
        VBox middleArea = new VBox(40, logoPane, startSection, destSection);
        middleArea.setAlignment(Pos.TOP_CENTER);

        // ---------- CONTROL BUTTONS ----------
        Button prevBtn = createControlButton("⏮");
        Button playBtn = createControlButton("▶");
        Button nextBtn = createControlButton("⏭");

        HBox controlButtons = new HBox(25, prevBtn, playBtn, nextBtn);
        controlButtons.setAlignment(Pos.CENTER);

        VBox bottomArea = new VBox(controlButtons);
        bottomArea.setPadding(new Insets(20, 0, 40, 0));
        bottomArea.setAlignment(Pos.BOTTOM_CENTER);

        // ---------- ADD TO BORDERPANE ----------
        container.setTop(topArea);
        container.setCenter(middleArea);
        container.setBottom(bottomArea);

        return new VBox(container);
    }


    private Button createControlButton(String text) {
        Button btn = new Button(text); btn.setPrefSize(50, 50);
        btn.setStyle("-fx-background-color: #1a5490; -fx-text-fill: white; -fx-font-size: 18px; " + "-fx-background-radius: 25; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #2d6bb8; -fx-text-fill: white; " + "-fx-font-size: 18px; -fx-background-radius: 25; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #1a5490; -fx-text-fill: white; " + "-fx-font-size: 18px; -fx-background-radius: 25; -fx-cursor: hand;"));
        return btn; }


    private Pane createMapPanel() {

        Pane mapPane = new Pane();
        mapPane.setStyle("-fx-background-color: #c5e8d5;");

        // Load your map image
        ImageView mapView = new ImageView(
                new Image("file:C:/Users/johar/IdeaProjects/JavaFx/src/main/java/com/example/javafx/Map_without_landmark1.png")
        );

        mapView.setPreserveRatio(true);
        mapView.setFitWidth(750);
        mapView.setLayoutX(5);
        mapView.setLayoutY(5);

        mapPane.getChildren().add(mapView);

        return mapPane;
    }


    private StackPane createBusStop(String name, double x, double y) {
        StackPane stop = new StackPane();

        Circle outerCircle = new Circle(20);
        outerCircle.setFill(Color.web("#E74C3C"));
        outerCircle.setStroke(Color.WHITE);
        outerCircle.setStrokeWidth(3);

        Label busIcon = new Label("🚌");
        busIcon.setStyle("-fx-font-size: 18px;");

        stop.getChildren().addAll(outerCircle, busIcon);
        stop.setLayoutX(x);
        stop.setLayoutY(y);

        return stop;
    }

    private StackPane createBusIndicator() {
        StackPane indicator = new StackPane();

        Circle circle = new Circle(15);
        circle.setFill(Color.web("#FFA500"));
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);

        Label icon = new Label("🚌");
        icon.setStyle("-fx-font-size: 16px;");

        indicator.getChildren().addAll(circle, icon);

        // Add animation
        TranslateTransition transition = new TranslateTransition(Duration.seconds(3), indicator);
        transition.setByX(150);
        transition.setByY(-100);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();

        return indicator;
    }

    private Label createCityLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        label.setLayoutX(x);
        label.setLayoutY(y);
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
