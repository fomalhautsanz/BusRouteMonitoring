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
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Terminal;
import model.TerminalEvent;
import model.Waypoint;
import view.components.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import App.MainApp;

public class BusRouteMonitoringSystem extends Application {

    private RouteManager routeManager = new RouteManager();
    private MapPanel mapPanel;

    private ImageView busIcon;

    private Image logoImage = new Image("file:assets/img/bus_logo.png");
    private Image busImage = new Image("file:assets/img/bus.png");

    private Terminal startTerminal = null;
    private Terminal endTerminal = null;

    private BorderPane leftPanelFull;
    private HBox leftPanelMinimized;
    private boolean isMinimized = false;
    private StackPane root;

    private ComboBox<String> startDropdown;
    private ComboBox<String> destDropdown;
    private ComboBox<String> historyDropdown = new ComboBox<>();

    private Timeline simClock;
    private Label simTimeLabel;
    private long accumulatedMillis = 0;
    private boolean simRunning = false;

    private Timeline timeline;
    private model.Bus bus;
    private boolean isBusPlaying = false;
    private Polyline busPathPolyline; 

    private ControlButton fullPanelBusPlayBtn;
    private ControlButton miniPanelBusPlayBtn;

    private final Set<Terminal> reachedTerminals = new HashSet<>();

    private TerminalInfoPanel terminalInfoPanel;
    
    private TerminalEventPanel eventPanel;
    private int disabledEventCount = 0;
    private static final int MAX_DISABLED_EVENTS_PER_ROUTE = 2;

    private List<Terminal> activeRouteTerminals = new ArrayList<>();

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

        terminalInfoPanel = new TerminalInfoPanel();
        terminalInfoPanel.setVisible(false);

        terminalInfoPanel.setOnClose(() -> terminalInfoPanel.setVisible(false));

        StackPane.setAlignment(terminalInfoPanel, Pos.CENTER_RIGHT);
        root.getChildren().add(terminalInfoPanel);


        StackPane.setAlignment(leftPanelMinimized, Pos.TOP_LEFT);
        leftPanelMinimized.setVisible(false);
        root.getChildren().add(leftPanelMinimized);

        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bus Route Monitoring System");
        primaryStage.show();

        

    }

    private BorderPane createLeftPanel(Pane mapPanel) {
        BorderPane container = new BorderPane();
        container.setPrefWidth(500);
        container.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 0 2 0 0;");
        container.setPadding(new Insets(20, 30, 20, 30));

        ControlButton closeBtn = new ControlButton("✕");
        ControlButton minimizeBtn = new ControlButton("—");

        closeBtn.setOnAction(e -> {
            try {
                Stage currentStage = (Stage) closeBtn.getScene().getWindow();
                currentStage.close();

                MainApp mainApp = new MainApp();
                Stage mainStage = new Stage();
                mainApp.start(mainStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        minimizeBtn.setOnAction(e -> toggleLeftPanel()); 
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

        prevBtn.setOnAction(e -> {
            rewindBus();
            if (isMinimized) toggleLeftPanel();
        });

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

    private void assignBusToRoute(Terminal start, Terminal end) {
        disabledEventCount = 0;
        reachedTerminals.clear();

        for (Terminal t : routeManager.getAllTerminals()) {
            t.setBusStatus("Idle");
            t.setLastArrivalTime("-");
            t.setDestination(null);
            t.setStopEnabled(true); 

        }
        terminalInfoPanel.setVisible(false);

        if (timeline != null) timeline.stop();
        if (busIcon != null) mapPanel.removeNodeFromMap(busIcon);
        if (busPathPolyline != null) mapPanel.removeNodeFromMap(busPathPolyline);

        List<Waypoint> rawRoute = routeManager.buildRoute(start, end);
        List<Waypoint> routePoints = new ArrayList<>();

        for (Waypoint wp : rawRoute) {
            routePoints.add(wp); 
        }

        activeRouteTerminals.clear();
        for (Waypoint wp : rawRoute) {
            for (Terminal t : routeManager.getAllTerminals()) {
                if (wp.getX() == t.getX() && wp.getY() == t.getY()) {
                    activeRouteTerminals.add(t);
                }
            }
        }


        if (routePoints == null || routePoints.size() < 2) {
            System.out.println("No valid route found.");
            return;
        }

        bus = new model.Bus(routePoints,0.3);

        busIcon = new ImageView(busImage);
        busIcon.setFitHeight(35);      
        busIcon.setPreserveRatio(true);
        busIcon.setSmooth(true);

        busIcon.setTranslateX(-busIcon.getBoundsInLocal().getWidth() / 2);
        busIcon.setTranslateY(-busIcon.getBoundsInLocal().getHeight() / 2);

        busIcon.setLayoutX(bus.getX());
        busIcon.setLayoutY(bus.getY());

        busPathPolyline = new Polyline();
        busPathPolyline.setStroke(Color.BLUE);
        busPathPolyline.setStrokeWidth(3);
        busPathPolyline.setOpacity(0.7);

        mapPanel.addNodeToMap(busPathPolyline);
        mapPanel.addNodeToMap(busIcon);

        for (Waypoint wp : routePoints) {
            busPathPolyline.getPoints().addAll(wp.getX(), wp.getY());
        }


        timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {

            if (!bus.isMoving()) return;

            double oldX = bus.getX();
            double oldY = bus.getY();

            bus.updatePosition();

            double dx = bus.getX() - oldX;
            double dy = bus.getY() - oldY;

            if (Math.abs(dx) > 0.001 || Math.abs(dy) > 0.001) {
                double angle = Math.toDegrees(Math.atan2(dy, dx));
                busIcon.setRotate(angle + 90); 
            }

            busIcon.setLayoutX(bus.getX());
            busIcon.setLayoutY(bus.getY());

            for (Terminal t : routeManager.getAllTerminals()) {

            if (!t.isActive() || !t.isStopEnabled()) continue;

            if (Math.abs(bus.getX() - t.getX()) < 1.5 &&
                Math.abs(bus.getY() - t.getY()) < 1.5) {

                handleTerminalArrival(t);
                break;
            }
        }

        }));


        timeline.setCycleCount(Timeline.INDEFINITE);

        isBusPlaying = false;
        updateBusPlayButtons("▶");

        mapPanel.bringMarkersToFront();
    }

    private void toggleBusAnimation(ControlButton btn, boolean togglePanel) {
        if (bus == null || timeline == null) {
            System.out.println("Please select a route first");
            return;
        }

        if (endTerminal != null && !endTerminal.isStopEnabled()) {
            System.out.println("Destination terminal is under attack! Bus cannot move.");
            return;
        }

        if (!isBusPlaying) {
            timeline.play();
            isBusPlaying = true;
            startSimClock();

            if (startTerminal != null) {
                startTerminal.setBusStatus("En Route");
                startTerminal.setDestination(endTerminal);
            }

            updateBusPlayButtons("⏸");
        } else {
            timeline.pause();
            stopSimClock();
            isBusPlaying = false;
            updateBusPlayButtons("▶");
        }

        if (togglePanel) toggleLeftPanel();
    }


    private void rewindBus() {
        if (bus == null || (endTerminal != null && !endTerminal.isStopEnabled())) return;

        double oldX = bus.getX();
        double oldY = bus.getY();

        if (timeline != null) timeline.pause();
        stopSimClock();
        isBusPlaying = false;
        updateBusPlayButtons("▶");

        bus.moveBackward();

        busIcon.setLayoutX(bus.getX());
        busIcon.setLayoutY(bus.getY());
        updateBusRotation(oldX, oldY);
    }

    private void forwardBus() {
        if (bus == null || (endTerminal != null && !endTerminal.isStopEnabled())) return;

        double oldX = bus.getX();
        double oldY = bus.getY();

        if (timeline != null) timeline.pause();
        stopSimClock();
        isBusPlaying = false;
        updateBusPlayButtons("▶");

        bus.skipForward(1);

        busIcon.setLayoutX(bus.getX());
        busIcon.setLayoutY(bus.getY());
        updateBusRotation(oldX, oldY);
    }


    private void updateBusRotation(double oldX, double oldY) {
        double dx = bus.getX() - oldX;
        double dy = bus.getY() - oldY;

        if (Math.abs(dx) > 0.001 || Math.abs(dy) > 0.001) {
            double angle = Math.toDegrees(Math.atan2(dy, dx));
            busIcon.setRotate(angle + 90);
        }
    }



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
        minimizedPanel.setPrefWidth(500);
        minimizedPanel.setPrefHeight(50);
        minimizedPanel.setMaxWidth(500);
        minimizedPanel.setMaxHeight(60);
        minimizedPanel.setAlignment(Pos.CENTER_LEFT);

        simTimeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        simTimeLabel.setMinWidth(60);
        simTimeLabel.setPrefWidth(70);
        simTimeLabel.setMaxWidth(80);
        simTimeLabel.setTranslateX(10);

        historyDropdown.setPrefWidth(220);
        historyDropdown.setPrefHeight(30);
        historyDropdown.setPromptText("Bus history");
        historyDropdown.setStyle("-fx-font-size: 12;");

        StackPane expandButton = new StackPane();
        expandButton.setPrefSize(25, 25);
        expandButton.setStyle("-fx-background-color: #1a5490; -fx-background-radius: 4;");
        Label expandIcon = new Label("⮟");
        expandIcon.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        expandButton.getChildren().add(expandIcon);
        expandButton.setOnMouseClicked(e -> toggleLeftPanel());
        expandButton.setPrefSize(75, 55);

        VBox expandBox = new VBox(expandButton);
        expandBox.setAlignment(Pos.CENTER);

        ControlButton prevBtn = new ControlButton("⏮");
        miniPanelBusPlayBtn = new ControlButton("▶");
        ControlButton nextBtn = new ControlButton("⏭");
        prevBtn.setPrefSize(65, 55);
        miniPanelBusPlayBtn.setPrefSize(55, 45);
        nextBtn.setPrefSize(65, 55);

        HBox controlButtons = new HBox(5, prevBtn, miniPanelBusPlayBtn, nextBtn);
        controlButtons.setAlignment(Pos.CENTER);

        miniPanelBusPlayBtn.setOnAction(e -> toggleBusAnimation(miniPanelBusPlayBtn, false));

        prevBtn.setOnAction(e -> rewindBus());

        nextBtn.setOnAction(e -> forwardBus());

        minimizedPanel.getChildren().addAll(simTimeLabel, historyDropdown, expandBox, controlButtons);

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
            if (t.isActive()) {
                mapPanel.addMarker(marker);
            }
        }
    }

    private void selectTerminal(Terminal t) {
        terminalInfoPanel.displayTerminal(t);

        if (startTerminal == null) {
            startTerminal = t;
            startDropdown.setValue(t.getName());
        } else if (endTerminal == null && t != startTerminal) {
            endTerminal = t;
            destDropdown.setValue(t.getName());
        }
    }



    private void startSimClock() {
        if (simClock != null) simClock.stop();

        simClock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

            int randomMinutes = ThreadLocalRandom.current().nextInt(1, 11);

            accumulatedMillis += randomMinutes * 60_000L;
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

    private void handleTerminalArrival(Terminal terminal) {
        if (reachedTerminals.contains(terminal)) return;
        reachedTerminals.add(terminal);

        Terminal panelTerminal = terminal;
        Terminal blockedTerminal = null; 

        timeline.pause();
        stopSimClock();
        isBusPlaying = false;
        updateBusPlayButtons("▶");

        String time = simTimeLabel.getText();

        TerminalEvent event;
        if (disabledEventCount < MAX_DISABLED_EVENTS_PER_ROUTE &&
            hasAnotherEnabledTerminal(terminal) &&
            Math.random() < 0.95) {

            event = TerminalEvent.disabledEvent();
            disabledEventCount++;

            int index = activeRouteTerminals.indexOf(terminal);
            if (index != -1) {
                if (index < activeRouteTerminals.size() - 1) {
                    blockedTerminal = activeRouteTerminals.get(index + 1);
                } else if (index > 0) {
                    blockedTerminal = activeRouteTerminals.get(index - 1);
                }
            }

            if (blockedTerminal != null) {
                blockedTerminal.setStopEnabled(false);
                panelTerminal = blockedTerminal;
                historyDropdown.getItems().add(
                    "⚠ " + blockedTerminal.getName() + " temporarily disabled"
                );
            }

        } else {
            event = TerminalEvent.normalEvent();
        }

        terminal.setBusStatus("Arrived");
        terminal.setLastArrivalTime(time);
        terminal.setDestination(endTerminal);

        if (terminal == endTerminal && !endTerminal.isStopEnabled()) {
            historyDropdown.getItems().add(
                "⚠ " + endTerminal.getName() + " is under attack! Bus stopped."
            );
            return; 
        }

        if (event.getType() == TerminalEvent.Type.DISABLED) {
            historyDropdown.getItems().add(
                "⚠ " + panelTerminal.getName() + ": " +
                event.getTitle() + " (" + time + ")"
            );
            showTerminalEventPanel(panelTerminal, event);
            terminalInfoPanel.setVisible(true);

        } else {
            historyDropdown.getItems().add(
                terminal.getName() + " arrived (" + time + ")"
            );
        }
    }

    private boolean hasAnotherEnabledTerminal(Terminal current) {
        for (Terminal t : activeRouteTerminals) {
            if (t != current && t.isStopEnabled()) {
                return true;
            }
        }
        return false;
    }


    private void showTerminalEventPanel(Terminal terminal, TerminalEvent event) {
        if (eventPanel != null) {
            root.getChildren().remove(eventPanel);
        }

        eventPanel = new TerminalEventPanel(terminal, event);

        eventPanel.setOnClose(() -> {
            root.getChildren().remove(eventPanel);
            eventPanel = null;
        });

        StackPane.setAlignment(eventPanel, Pos.CENTER);
        root.getChildren().add(eventPanel);
    }

}