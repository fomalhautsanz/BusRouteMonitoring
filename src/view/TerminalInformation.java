package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TerminalInformation extends Application {

    public void start(Stage primaryStage) {
        // Main container
        HBox mainContainer = new HBox();
        mainContainer.setStyle(
                "-fx-background-image: url('file:C:/Users/johar/IdeaProjects/JavaFx/src/main/java/com/example/javafx/maximize_map.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center center;");

        // Left information panel
        VBox infoPanel = createInfoPanel();

        // Spacer to push map content to the right
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        mainContainer.getChildren().addAll(infoPanel, spacer);

        Scene scene = new Scene(mainContainer, 1200, 700);
        primaryStage.setTitle("Bus Route Monitoring System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createInfoPanel() {
        VBox panel = new VBox(25);
        panel.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 0;" +
                        "-fx-padding: 25;" +
                        "-fx-border-color: #4A90E2;" +
                        "-fx-border-width: 0 4px 0 0;"
        );
        panel.setPrefWidth(300);
        panel.setAlignment(Pos.TOP_LEFT);

        // Terminal Name Section
        VBox terminalSection = createInfoSection(
                "🚏",
                "Terminal Name:",
                "TML-102 – Terminal"
        );

        // Location Section
        VBox locationSection = createInfoSection(
                "📍",
                "Location:",
                "Digos City"
        );

        // Bus Status Section
        VBox statusSection = createInfoSection(
                "🚌",
                "Bus Status:",
                "Not Arrived"
        );

        // Departure Time Section
        VBox departureSection = createInfoSection(
                "🕐",
                "Departure Time:",
                "2:30 PM"
        );

        // Destination Section
        VBox destinationSection = createInfoSection(
                "📌",
                "Destination:",
                "Tagum City"
        );

        panel.getChildren().addAll(
                terminalSection,
                locationSection,
                statusSection,
                departureSection,
                destinationSection
        );

        return panel;
    }

    private VBox createInfoSection(String icon, String label, String value) {
        VBox section = new VBox(8);

        // Header with icon and label
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #4A90E2;");

        Label labelText = new Label(label);
        labelText.setFont(Font.font("System", FontWeight.BOLD, 15));
        labelText.setStyle("-fx-text-fill: #666666;");

        header.getChildren().addAll(iconLabel, labelText);

        // Value text
        Label valueText = new Label(value);
        valueText.setFont(Font.font("System", FontWeight.NORMAL, 16));
        valueText.setStyle("-fx-text-fill: #333333;");
        valueText.setWrapText(true);

        section.getChildren().addAll(header, valueText);

        return section;
    }
}