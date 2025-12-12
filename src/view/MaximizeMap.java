package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MaximizeMap extends Application {

    private ComboBox<String> statusDropdown;
    private Label timeLabel;

    public void start(Stage primaryStage) {
        // Main container
        VBox mainContainer = new VBox(10);
        mainContainer.setStyle(
                "-fx-background-image: url('file:C:\\Users\\Mind Motion Films\\Documents\\GitHub\\BusRouteMonitoring\\src\\main\\java\\mainUI\\maximize_map.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center center;");

        // Top control panel
        HBox topPanel = createTopControlPanel();
        topPanel.setAlignment(Pos.TOP_LEFT);
        topPanel.setPadding(new Insets(15));

        mainContainer.getChildren().add(topPanel);

        Scene scene = new Scene(mainContainer, 1200, 700);
        primaryStage.setTitle("Bus Route Monitoring System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTopControlPanel() {
        HBox panel = new HBox(15);
        panel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 10;");
        panel.setAlignment(Pos.CENTER_LEFT);


        // Status dropdown
        statusDropdown = new ComboBox<>();
        statusDropdown.getItems().addAll(
                "Bus has started travelling"
        );

        statusDropdown.setValue("10:00 AM");
        statusDropdown.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        statusDropdown.setPrefWidth(200);

        // Control buttons
        Button prevButton = createControlButton("⏮");
        Button playButton = createControlButton("▶");
        Button nextButton = createControlButton("⏭");

        // Button actions
        prevButton.setOnAction(e -> handlePrevious());
        playButton.setOnAction(e -> handlePlay(playButton));
        nextButton.setOnAction(e -> handleNext());

        // Fullscreen button
        Button fullscreenButton = createControlButton("⛶");
        fullscreenButton.setOnAction(e -> handleFullscreen());

        panel.getChildren().addAll(
                statusDropdown,
                prevButton,
                playButton,
                nextButton,
                fullscreenButton
        );

        return panel;
    }

    private Button createControlButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #4A90E2; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-color: #4A90E2; " +
                        "-fx-border-radius: 4; " +
                        "-fx-background-radius: 4; " +
                        "-fx-padding: 5 10; " +
                        "-fx-font-size: 14px; " +
                        "-fx-cursor: hand;");

        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: #357ABD; " +
                                "-fx-text-fill: white; " +
                                "-fx-border-color: #357ABD; " +
                                "-fx-border-radius: 4; " +
                                "-fx-background-radius: 4; " +
                                "-fx-padding: 5 10; " +
                                "-fx-font-size: 14px; " +
                                "-fx-cursor: hand;"));
        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: #4A90E2; " +
                                "-fx-text-fill: white; " +
                                "-fx-border-color: #4A90E2; " +
                                "-fx-border-radius: 4; " +
                                "-fx-background-radius: 4; " +
                                "-fx-padding: 5 10; " +
                                "-fx-font-size: 14px; " +
                                "-fx-cursor: hand;"));

        return button;
    }

    private void handlePrevious() {
        System.out.println("Previous button clicked");
        // Implement previous timestamp logic
    }

    private void handlePlay(Button playButton) {
        if (playButton.getText().equals("▶")) {
            playButton.setText("⏸");
            System.out.println("Play started");
            // Implement play/animation logic
        } else {
            playButton.setText("▶");
            System.out.println("Paused");
            // Implement pause logic
        }
    }

    private void handleNext() {
        System.out.println("Next button clicked");
        // Implement next timestamp logic
    }

    private void handleFullscreen() {
        System.out.println("Fullscreen toggled");
        // Implement fullscreen toggle logic
    }
}