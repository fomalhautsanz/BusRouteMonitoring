package view.Information;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BusTerminalPanel extends VBox {

    public BusTerminalPanel() {
        this("TML-102 – Terminal", "Digos City", "Not Arrived", "2:30 PM", "Tagum City");
    }

    public BusTerminalPanel(String terminalName, String location, String busStatus,
                            String departureTime, String destination) {
        // Panel styling
        setSpacing(24);
        setPadding(new Insets(24));
        setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #3B82F6; " +
                "-fx-border-width: 4; " +
                "-fx-border-radius: 10;");
        setPrefWidth(288);
        setMaxWidth(288);

        // Add info rows
        getChildren().addAll(
                createInfoRow("📍", "Terminal Name:", terminalName),
                createInfoRow("🌐", "Location:", location),
                createInfoRow("🚌", "Bus Status:", busStatus),
                createInfoRow("🕐", "Departure Time:", departureTime),
                createInfoRow("📌", "Destination:", destination)
        );
    }

    private HBox createInfoRow(String icon, String label, String value) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.TOP_LEFT);

        // Icon circle
        StackPane iconPane = new StackPane();
        Circle circle = new Circle(20);
        circle.setFill(Color.web("#60A5FA"));

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Segoe UI Emoji", 20));
        iconLabel.setStyle("-fx-text-fill: white;");

        iconPane.getChildren().addAll(circle, iconLabel);
        iconPane.setMinSize(40, 40);
        iconPane.setMaxSize(40, 40);

        // Text container
        VBox textBox = new VBox(2);

        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label valueText = new Label(value);
        valueText.setFont(Font.font("Arial", 14));
        valueText.setStyle("-fx-text-fill: #374151;");

        textBox.getChildren().addAll(labelText, valueText);

        row.getChildren().addAll(iconPane, textBox);

        return row;
    }

    // Update methods for dynamic changes
    public void updateTerminalName(String terminalName) {
        updateRow(0, terminalName);
    }

    public void updateLocation(String location) {
        updateRow(1, location);
    }

    public void updateBusStatus(String busStatus) {
        updateRow(2, busStatus);
    }

    public void updateDepartureTime(String departureTime) {
        updateRow(3, departureTime);
    }

    public void updateDestination(String destination) {
        updateRow(4, destination);
    }

    private void updateRow(int index, String newValue) {
        if (index < getChildren().size()) {
            HBox row = (HBox) getChildren().get(index);
            VBox textBox = (VBox) row.getChildren().get(1);
            Label valueLabel = (Label) textBox.getChildren().get(1);
            valueLabel.setText(newValue);
        }
    }
}