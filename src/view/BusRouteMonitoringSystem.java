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
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Route;
import model.Terminal;
import model.Waypoint;
import view.components.*;
import model.LinkedList;

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

    // Bus animation
    private Timeline timeline;
    private model.Bus bus;
    private boolean isBusPlaying = false;
    private Polyline busPathPolyline; // NEW: Polyline to show bus path
    private java.util.List<Double> fullRoutePoints; // NEW: Store all points of the full route
    private int currentRouteIndex; // NEW: Track current position in the route

    // Store references to play buttons for bus animation
    private ControlButton fullPanelBusPlayBtn;
    private ControlButton miniPanelBusPlayBtn;


    @Override
    public void start(Stage primaryStage) {
        mapPanel = new MapPanel("file:assets/img/map.png");

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
        fullPanelBusPlayBtn = new ControlButton("▶");
        ControlButton nextBtn = new ControlButton("⏭");

        fullPanelBusPlayBtn.setOnAction(e -> toggleBusAnimation(fullPanelBusPlayBtn, true));

        // MODIFIED: Rewind button for bus
        prevBtn.setOnAction(e -> {
            rewindBus();
            if (isMinimized) toggleLeftPanel();
        });

        // MODIFIED: Forward button for bus
        nextBtn.setOnAction(e -> {
            forwardBus();
            if (isMinimized) toggleLeftPanel();
        });

        HBox controlButtons = new HBox(25, prevBtn, fullPanelBusPlayBtn, nextBtn);
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

    // MODIFIED: Bus animation setup without auto-play
    private void assignBusToRoute(Terminal start, Terminal end) {
        // Stop and clean up existing animation
        if (timeline != null) {
            timeline.stop();
        }
        if (busIcon != null) {
            mapPanel.removeNodeFromMap(busIcon);
        }
        if (busPathPolyline != null) {
            mapPanel.removeNodeFromMap(busPathPolyline);
        }

        LinkedList<Waypoint>.Node startNode = routeManager.getNodeForTerminal(start);
        LinkedList<Waypoint>.Node endNode = routeManager.getNodeForTerminal(end);

        if (startNode == null || endNode == null) return;

        // NEW: Determine if we're going forward or backward through waypoints
        boolean isReversed = isRouteReversed(startNode, endNode);

        // Create new bus
        bus = new model.Bus(startNode, endNode, 1.0);

        // Create bus icon
        busIcon = new Circle(8, Color.ORANGE);
        busIcon.setLayoutX(bus.getX());
        busIcon.setLayoutY(bus.getY());
        mapPanel.addNodeToMap(busIcon);

        // NEW: Pre-calculate the FULL route by simulating bus movement
        fullRoutePoints = new java.util.ArrayList<>();

        if (isReversed) {
            // Build route going backward through waypoints
            LinkedList<Waypoint>.Node current = startNode;
            while (current != null) {
                fullRoutePoints.add(current.data.getX());
                fullRoutePoints.add(current.data.getY());
                if (current == endNode) break;
                current = current.prev; // Go backward through linked list
            }
        } else {
            // Build route going forward through waypoints (original behavior)
            model.Bus tempBus = new model.Bus(startNode, endNode, 1.0);
            while (tempBus.isMoving()) {
                fullRoutePoints.add(tempBus.getX());
                fullRoutePoints.add(tempBus.getY());
                tempBus.updatePosition();
            }
            // Add final position
            fullRoutePoints.add(tempBus.getX());
            fullRoutePoints.add(tempBus.getY());
        }

        // Initialize route index
        currentRouteIndex = 0;

        // Create polyline with the full pre-calculated route
        busPathPolyline = new Polyline();
        busPathPolyline.setStroke(Color.BLUE);
        busPathPolyline.setStrokeWidth(3);
        busPathPolyline.setOpacity(0.7);
        busPathPolyline.getPoints().addAll(fullRoutePoints);

        mapPanel.addNodeToMap(busPathPolyline);

        // Create timeline but DON'T start it yet
        timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            if (isReversed) {
                bus.moveBackward(); // Move backward through waypoints
            } else {
                bus.updatePosition(); // Move forward through waypoints
            }
            busIcon.setLayoutX(bus.getX());
            busIcon.setLayoutY(bus.getY());

            // NEW: Remove points from the front and track index
            if (busPathPolyline.getPoints().size() > 2 && currentRouteIndex < fullRoutePoints.size() - 2) {
                busPathPolyline.getPoints().remove(0, 2);
                currentRouteIndex += 2; // Track consumed points
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        // Reset playing state
        isBusPlaying = false;
        updateBusPlayButtons("▶");
    }

    // NEW: Check if route is reversed (going backward through waypoints)
    private boolean isRouteReversed(LinkedList<Waypoint>.Node startNode, LinkedList<Waypoint>.Node endNode) {
        // Search forward from startNode
        LinkedList<Waypoint>.Node current = startNode;
        while (current != null) {
            if (current == endNode) return false; // Found end going forward
            current = current.next;
        }

        // If not found going forward, must be going backward
        return true;
    }

    // NEW: Toggle bus animation play/pause
    private void toggleBusAnimation(ControlButton btn, boolean togglePanel) {
        // Check if bus exists
        if (bus == null || timeline == null) {
            System.out.println("Please select a route first");
            return;
        }

        if (!isBusPlaying) {
            // Start playing
            timeline.play();
            isBusPlaying = true;
            updateBusPlayButtons("⏸");
        } else {
            // Pause
            timeline.pause();
            isBusPlaying = false;
            updateBusPlayButtons("▶");
        }

        if (togglePanel) toggleLeftPanel();
    }

    // NEW: Rewind bus (move backwards along route)
    private void rewindBus() {
        System.out.println("=== rewindBus() called ===");
        if (bus == null || fullRoutePoints == null) {
            System.out.println("Please select a route first");
            return;
        }

        // Store the current position before moving
        double beforeX = bus.getX();
        double beforeY = bus.getY();

        bus.moveBackward(); // Move back 1 waypoint only

        // Calculate how far the bus moved
        double afterX = bus.getX();
        double afterY = bus.getY();

        busIcon.setLayoutX(afterX);
        busIcon.setLayoutY(afterY);

        // Find how many points to add back by calculating distance
        // We need to add points until we reach the bus's new position
        int pointsToAdd = 0;
        int tempIndex = currentRouteIndex - 2;

        while (tempIndex >= 0) {
            double px = fullRoutePoints.get(tempIndex);
            double py = fullRoutePoints.get(tempIndex + 1);

            // Check if this point matches the bus's new position (roughly)
            double dist = Math.sqrt(Math.pow(px - afterX, 2) + Math.pow(py - afterY, 2));
            if (dist < 5.0) { // Within 5 pixels
                break;
            }

            pointsToAdd += 2;
            tempIndex -= 2;
        }

        // Add points back to the polyline
        for (int i = 0; i < pointsToAdd && currentRouteIndex >= 2; i += 2) {
            currentRouteIndex -= 2;
            double x = fullRoutePoints.get(currentRouteIndex);
            double y = fullRoutePoints.get(currentRouteIndex + 1);
            busPathPolyline.getPoints().add(0, y);
            busPathPolyline.getPoints().add(0, x);
        }

        System.out.println("=== rewindBus() finished, added " + pointsToAdd + " points ===");
    }

    // NEW: Forward bus (skip ahead along route)
    private void forwardBus() {
        if (bus == null || fullRoutePoints == null) {
            System.out.println("Please select a route first");
            return;
        }

        // Store the current position before moving
        double beforeX = bus.getX();
        double beforeY = bus.getY();

        bus.skipForward(1); // Skip forward 1 waypoint only

        // Calculate how far the bus moved
        double afterX = bus.getX();
        double afterY = bus.getY();

        busIcon.setLayoutX(afterX);
        busIcon.setLayoutY(afterY);

        // Find how many points to remove by calculating distance
        int pointsToRemove = 0;
        int tempIndex = currentRouteIndex;

        while (tempIndex < fullRoutePoints.size() - 2) {
            double px = fullRoutePoints.get(tempIndex);
            double py = fullRoutePoints.get(tempIndex + 1);

            // Check if this point matches the bus's new position (roughly)
            double dist = Math.sqrt(Math.pow(px - afterX, 2) + Math.pow(py - afterY, 2));
            if (dist < 5.0) { // Within 5 pixels
                break;
            }

            pointsToRemove += 2;
            tempIndex += 2;
        }

        // Remove points from the polyline
        for (int i = 0; i < pointsToRemove && busPathPolyline.getPoints().size() >= 2 && currentRouteIndex < fullRoutePoints.size() - 2; i += 2) {
            busPathPolyline.getPoints().remove(0, 2);
            currentRouteIndex += 2;
        }

        System.out.println("=== forwardBus() finished, removed " + pointsToRemove + " points ===");
    }

    // NEW: Update both play buttons for bus animation
    private void updateBusPlayButtons(String text) {
        if (fullPanelBusPlayBtn != null) {
            fullPanelBusPlayBtn.setText(text);
        }
        if (miniPanelBusPlayBtn != null) {
            miniPanelBusPlayBtn.setText(text);
        }
    }


    private void handleSelection(Pane mapPanelParam, ComboBox<String> startDropdown, ComboBox<String> endDropdown) {
        String startName = startDropdown.getValue();
        String endName = endDropdown.getValue();

        if (startName != null && endName != null && !startName.equals(endName)) {
            startTerminal = routeManager.getTerminalByName(startName);
            endTerminal = routeManager.getTerminalByName(endName);

            if (startTerminal != null && endTerminal != null) {
                assignBusToRoute(startTerminal, endTerminal);
            }
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
        miniPanelBusPlayBtn = new ControlButton("▶");
        ControlButton nextBtn = new ControlButton("⏭");
        prevBtn.setPrefSize(50, 40);
        miniPanelBusPlayBtn.setPrefSize(50, 40);
        nextBtn.setPrefSize(50, 40);

        HBox controlButtons = new HBox(5, prevBtn, miniPanelBusPlayBtn, nextBtn);
        controlButtons.setAlignment(Pos.CENTER);

        // Button actions - MODIFIED for bus animation
        miniPanelBusPlayBtn.setOnAction(e -> toggleBusAnimation(miniPanelBusPlayBtn, false));

        // MODIFIED: Rewind button for bus
        prevBtn.setOnAction(e -> rewindBus());

        // MODIFIED: Forward button for bus
        nextBtn.setOnAction(e -> forwardBus());

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
            startDropdown.setValue(t.getName());
        } else {
            endTerminal = t;
            destDropdown.setValue(t.getName());

            // Instead of finding a Route, just assign bus using waypoints
            if (startTerminal != null && endTerminal != null) {
                assignBusToRoute(startTerminal, endTerminal);
            }

            // Do NOT reset startTerminal here, allow chaining or re-selection
            // startTerminal = null;
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
        } else {
            stopSimClock();
            btn.setText("▶");
            updateOtherPlayButtons("▶", btn);
        }
        if (togglePanel) toggleLeftPanel();
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