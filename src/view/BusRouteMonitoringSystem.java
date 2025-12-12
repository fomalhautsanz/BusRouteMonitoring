package view;

import controller.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Route;
import model.Terminal;
import view.components.*;

public class BusRouteMonitoringSystem extends Application {

    private RouteManager routeManager = new RouteManager();
    private MapPanel mapPanel;
    
    private Circle busIcon; 
    
    // Kani wla sa assets/img/ paki addn alang. Gamit ug same na lcoation as below kugn magkuha img
    private Image logoImage = new Image("file:assets/img/bus_logo.png");
   
    // Terminal
    private Terminal startTerminal = null;
    private Terminal endTerminal = null;
    
    // Panel
    private BorderPane leftPanelFull;      
    private HBox leftPanelMinimized;       
    private boolean isMinimized = false;  
    private StackPane root;                

    private ComboBox<String> startDropdown;
    private ComboBox<String> destDropdown;

    // Time
    private Timeline simClock;
    private Label simTimeLabel;
    private boolean simRunning = false;
    private long accumulatedMillis = 0; 


    @Override
    public void start(Stage primaryStage) {
        mapPanel = new MapPanel("file:assets/img/map.png");
        mapPanel.setOnMouseClicked(e -> {
            System.out.println("Clicked map at: " + e.getX() + ", " + e.getY());
        });

        addMarkers();

        leftPanelFull = createLeftPanel(mapPanel);
        leftPanelFull.setPrefWidth(400);
        leftPanelFull.setMaxWidth(400);


        java.time.LocalTime now = java.time.LocalTime.now();
        accumulatedMillis = (now.getHour() * 60 + now.getMinute()) * 60_000L;

        simTimeLabel = new Label();
        simTimeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        simTimeLabel.setMinWidth(60);
        simTimeLabel.setPrefWidth(70);
        simTimeLabel.setMaxWidth(80);

        updateSimTimeLabel(accumulatedMillis);

        leftPanelMinimized = createMinimizedLeftPanel();

        root = new StackPane();
        root.getChildren().add(mapPanel);                    
        StackPane.setAlignment(leftPanelFull, Pos.CENTER_LEFT);
        root.getChildren().add(leftPanelFull);               

        StackPane.setAlignment(leftPanelMinimized, Pos.TOP_LEFT);
        leftPanelMinimized.setVisible(false);
        root.getChildren().add(leftPanelMinimized); 

        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bus Route Monitoring System");
        primaryStage.show();
        
    }

    // ---------------- LEFT PANEL ----------------
    private BorderPane createLeftPanel(Pane mapPanel) {
        BorderPane container = new BorderPane();
        container.setPrefWidth(500);
        container.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 0 2 0 0;");
        container.setPadding(new Insets(20, 30, 20, 30));

        ControlButton closeBtn = new ControlButton("✕");
        ControlButton minimizeBtn = new ControlButton("—");
        minimizeBtn.setOnAction(e -> toggleLeftPanel()); // toggle to minimized panel       
        
        HBox topButtons = new HBox(15, closeBtn, minimizeBtn);
        topButtons.setAlignment(Pos.TOP_LEFT);

        VBox topArea = new VBox(20, topButtons);
        topArea.setAlignment(Pos.TOP_LEFT);


        Pane logoPane = new Pane();
        logoPane.setPrefHeight(180);

        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(140);
        logoView.setPreserveRatio(true);
        logoView.setLayoutX(200);
        logoView.setLayoutY(10);

        Label titleLabel = new Label("BUS ROUTE\nMONITORING SYSTEM");
        titleLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.web("#1a5490"));
        titleLabel.setLayoutX(40);
        titleLabel.setLayoutY(100);

        logoPane.getChildren().addAll(logoView, titleLabel);


        Label startLabel = new Label("START TERMINAL");
        startLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        startLabel.setTextFill(Color.web("#1a5490"));

        startDropdown = new ComboBox<>();
        startDropdown.setPrefSize(300, 40);
        startDropdown.getItems().addAll(routeManager.getTerminalNames());
        startDropdown.setPromptText("Select start terminal");

        VBox startSection = new VBox(10, startLabel, startDropdown);
        startSection.setAlignment(Pos.CENTER);


        Label destLabel = new Label("DESTINATION");
        destLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        destLabel.setTextFill(Color.web("#1a5490"));

        destDropdown = new ComboBox<>();
        destDropdown.setPrefSize(300, 40);
        destDropdown.getItems().addAll(routeManager.getTerminalNames());
        destDropdown.setPromptText("Select end terminal");

        VBox destSection = new VBox(10, destLabel, destDropdown);
        destSection.setAlignment(Pos.CENTER);


        VBox middleArea = new VBox(40, logoPane, startSection, destSection);
        middleArea.setAlignment(Pos.TOP_CENTER);


        ControlButton prevBtn = new ControlButton("⏮");
        ControlButton playBtn = new ControlButton("▶");
        ControlButton nextBtn = new ControlButton("⏭");

        playBtn.setOnAction(e -> toggleSimulation(playBtn, true));

        nextBtn.setOnAction(e -> {
            startSimClock();
            accumulatedMillis += 60_000;
            updateSimTimeLabel(accumulatedMillis);
            toggleLeftPanel();
        });

        prevBtn.setOnAction(e -> {
            startSimClock();
            accumulatedMillis -= 60_000;
            if (accumulatedMillis < 0) accumulatedMillis = 0;
            updateSimTimeLabel(accumulatedMillis);
            toggleLeftPanel();
        });

        HBox controlButtons = new HBox(25, prevBtn, playBtn, nextBtn);
        controlButtons.setAlignment(Pos.CENTER);

        VBox bottomArea = new VBox(controlButtons);
        bottomArea.setPadding(new Insets(20, 0, 40, 0));
        bottomArea.setAlignment(Pos.BOTTOM_CENTER);


        container.setTop(topArea);
        container.setCenter(middleArea);
        container.setBottom(bottomArea);

        startDropdown.setOnAction(e ->
                handleSelection(mapPanel, startDropdown, destDropdown));

        destDropdown.setOnAction(e ->
                handleSelection(mapPanel, startDropdown, destDropdown));


        return container;
    }

    private void assignBusToRoute(Pane mapPanel, model.Route route) {
        if (busIcon != null) mapPanel.getChildren().remove(busIcon);

        model.Bus bus = new model.Bus(route, 2.0);
        busIcon = new Circle(bus.getX(), bus.getY(), 8, Color.ORANGE);
        mapPanel.getChildren().add(busIcon);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            bus.updatePosition();
            busIcon.setLayoutX(bus.getX());
            busIcon.setLayoutY(bus.getY());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void handleSelection(Pane mapPanel, ComboBox<String> startDropdown, ComboBox<String> endDropdown) {
        String startName = startDropdown.getValue();
        String endName = endDropdown.getValue();

        if (startName != null && endName != null && !startName.equals(endName)) {
            startTerminal = routeManager.getTerminalByName(startName);
            endTerminal = routeManager.getTerminalByName(endName);

            model.Route selectedRoute = routeManager.findRoute(startTerminal, endTerminal);
            if (selectedRoute != null) assignBusToRoute(mapPanel, selectedRoute);
        }
    }

    private HBox createMinimizedLeftPanel() {
        HBox minimizedPanel = new HBox(10); 
        minimizedPanel.setPadding(new Insets(5, 10, 5, 10));
        minimizedPanel.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 0; -fx-background-radius: 8;");
        minimizedPanel.setPrefWidth(400);
        minimizedPanel.setPrefHeight(50);
        minimizedPanel.setMaxWidth(400);
        minimizedPanel.setMaxHeight(60);
        minimizedPanel.setAlignment(Pos.CENTER_LEFT);

        simTimeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        simTimeLabel.setMinWidth(60);
        simTimeLabel.setPrefWidth(70);
        simTimeLabel.setMaxWidth(80);
        simTimeLabel.setTranslateX(10);

        ComboBox<String> dropdown = new ComboBox<>();
        dropdown.setPrefWidth(120);
        dropdown.setPrefHeight(30);
        dropdown.setPromptText("Select...");
        dropdown.setStyle("-fx-font-size: 12;");

        StackPane expandButton = new StackPane();
        expandButton.setPrefSize(25, 25);
        expandButton.setStyle("-fx-background-color: #1a5490; -fx-background-radius: 4;");
        Label expandIcon = new Label("⮟");
        expandIcon.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        expandButton.getChildren().add(expandIcon);
        expandButton.setOnMouseClicked(e -> toggleLeftPanel());

        VBox expandBox = new VBox(expandButton);
        expandBox.setAlignment(Pos.CENTER);

        ControlButton prevBtn = new ControlButton("⏮");
        ControlButton playBtn = new ControlButton("▶");
        ControlButton nextBtn = new ControlButton("⏭");
        prevBtn.setPrefSize(50, 40);
        playBtn.setPrefSize(50, 40);
        nextBtn.setPrefSize(50, 40);

        HBox controlButtons = new HBox(5, prevBtn, playBtn, nextBtn);
        controlButtons.setAlignment(Pos.CENTER);

        // Button actions
        playBtn.setOnAction(e -> toggleSimulation(playBtn, false));


        nextBtn.setOnAction(e -> {
            accumulatedMillis += 60_000;
            updateSimTimeLabel(accumulatedMillis);
        });

        prevBtn.setOnAction(e -> {
            accumulatedMillis -= 60_000;
            if (accumulatedMillis < 0) accumulatedMillis = 0;
            updateSimTimeLabel(accumulatedMillis);
        });

        minimizedPanel.getChildren().addAll(simTimeLabel, dropdown, expandBox, controlButtons);

        StackPane.setAlignment(minimizedPanel, Pos.TOP_LEFT);
        StackPane.setMargin(minimizedPanel, new Insets(20, 0, 0, 20));

        return minimizedPanel;
    }

    private void toggleLeftPanel() {
        if (isMinimized) {
            leftPanelMinimized.setVisible(false);
            leftPanelFull.setVisible(true);
            isMinimized = false;
        } else {
            leftPanelFull.setVisible(false);
            leftPanelMinimized.setVisible(true);
            isMinimized = true;
        }
    }

    private void addMarkers() {
        for (Terminal t : routeManager.getAllTerminals()) {
            MapMarker marker = new MapMarker(t, () -> selectTerminal(t));
            mapPanel.addMarker(marker);
        }
    }


    private void selectTerminal(Terminal t) {
        if (startTerminal == null) {
            startTerminal = t;
            System.out.println("Start: " + t.getName());
            startDropdown.setValue(t.getName());
        } else {
            Terminal endTerminal = t;
            System.out.println("End: " + endTerminal.getName());
            destDropdown.setValue(t.getName());

            Route route = routeManager.findRoute(startTerminal, endTerminal);
            if (route != null) assignBusToRoute(mapPanel, route);
            startTerminal = null; 
        }
    }

    private void startSimClock() {
        if (simClock != null) simClock.stop();

        simClock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            accumulatedMillis += 60_000;
            updateSimTimeLabel(accumulatedMillis);
        }));

        simClock.setCycleCount(Timeline.INDEFINITE);
        simClock.play();
        simRunning = true;
    }

    private void stopSimClock() {
        if (simClock != null) simClock.stop();
        simRunning = false;
    }

    private void updateSimTimeLabel(long totalMillis) {
        long totalMinutes = totalMillis / 60000;
        long hours = totalMinutes / 60 % 24;  
        long minutes = totalMinutes % 60;

        String formatted = String.format("%02d:%02d", hours, minutes);
        simTimeLabel.setText(formatted);
    }

    private void toggleSimulation(ControlButton btn, boolean togglePanel) {
        if (!simRunning) {
            startSimClock();
            btn.setText("⏸");
            updateOtherPlayButtons("⏸", btn);
            if (togglePanel) toggleLeftPanel();
        } else {
            stopSimClock();
            btn.setText("▶");
            updateOtherPlayButtons("▶", btn);
            if (togglePanel) toggleLeftPanel();
        }
}


    private void updateOtherPlayButtons(String text, ControlButton clickedBtn) {
        if (leftPanelFull != null) {
            HBox bottomControls = (HBox)((VBox)leftPanelFull.getBottom()).getChildren().get(0);
            ControlButton fullBtn = (ControlButton) bottomControls.getChildren().get(1);
            if (fullBtn != clickedBtn) fullBtn.setText(text);
        }
        if (leftPanelMinimized != null) {
            HBox controls = (HBox) leftPanelMinimized.getChildren().get(3); 
            ControlButton miniBtn = (ControlButton) controls.getChildren().get(1);
            if (miniBtn != clickedBtn) miniBtn.setText(text);
        }
    }
}
