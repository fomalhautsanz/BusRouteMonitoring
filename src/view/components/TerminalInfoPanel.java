package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Terminal;

public class TerminalInfoPanel extends BorderPane {

    private Terminal terminal;

    private Label terminalNameLabel;
    private Label locationLabel;
    private Label busStatusLabel;
    private Label arrivalTimeLabel;
    private Label destinationLabel;

    private Runnable onClose;

    public TerminalInfoPanel() {
        initializeUI();
        setVisible(false);
    }

    public TerminalInfoPanel(Terminal terminal) {
        initializeUI();
        displayTerminal(terminal);
    }

    private void initializeUI() {
        setMinWidth(400);
        setMaxWidth(400);

        setPrefWidth(400);
        setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 2;");
        setPadding(new Insets(20, 30, 20, 30));

        setTop(createTopArea());
        setCenter(createInfoContent());
    }

    private VBox createTopArea() {
        ControlButton closeBtn = new ControlButton("✕");
        closeBtn.setOnAction(e -> {
            if (onClose != null) onClose.run();
        });

        HBox topButtons = new HBox(15, closeBtn);
        topButtons.setAlignment(Pos.TOP_RIGHT);

        VBox topArea = new VBox(20, topButtons);
        topArea.setAlignment(Pos.TOP_RIGHT);

        return topArea;
    }

    private VBox createInfoContent() {
        VBox infoContent = new VBox(20);
        infoContent.setAlignment(Pos.TOP_LEFT);
        infoContent.setPadding(new Insets(20, 0, 0, 0));

        Label titleLabel = new Label("TERMINAL INFORMATION");
        titleLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#1a5490"));

        HBox nameSection = createInfoRow("📍", "Terminal Name:");
        terminalNameLabel = getValueLabel(nameSection);

        HBox locationSection = createInfoRow("🗺️", "Location:");
        locationLabel = getValueLabel(locationSection);

        HBox statusSection = createInfoRow("🚌", "Bus Status:");
        busStatusLabel = getValueLabel(statusSection);

        HBox timeSection = createInfoRow("🕐", "Arrival Time:");
        arrivalTimeLabel = getValueLabel(timeSection);

        HBox destSection = createInfoRow("🎯", "Destination:");
        destinationLabel = getValueLabel(destSection);

        infoContent.getChildren().addAll(
                titleLabel,
                new Label(""),
                nameSection,
                locationSection,
                statusSection,
                timeSection,
                destSection
        );

        return infoContent;
    }

    private HBox createInfoRow(String icon, String title) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 20px;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));

        Label valueLabel = new Label("—");
        valueLabel.setFont(javafx.scene.text.Font.font("Arial", 14));

        VBox textBox = new VBox(5, titleLabel, valueLabel);
        row.getChildren().addAll(iconLabel, textBox);

        return row;
    }

    public void displayTerminal(Terminal terminal) {
        if (terminal == null) return;

        this.terminal = terminal;

        terminalNameLabel.setText(terminal.getName());
        locationLabel.setText(extractLocation(terminal.getName()));
        busStatusLabel.setText(terminal.getBusStatus());
        arrivalTimeLabel.setText(terminal.getLastArrivalTime());

        if (terminal.getDestination() != null) {
            destinationLabel.setText(terminal.getDestination().getName());
        } else {
            destinationLabel.setText("—");
        }

        setVisible(true);
    }

    public void refresh() {
        if (terminal != null) displayTerminal(terminal);
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }

    private Label getValueLabel(HBox row) {
        return (Label) ((VBox) row.getChildren().get(1)).getChildren().get(1);
    }

    private String extractLocation(String terminalName) {
        if (terminalName.contains("Bislig")) return "Surigao del Sur";
        if (terminalName.contains("Monkayo")) return "Davao de Oro";
        if (terminalName.contains("Nabunturan")) return "Davao de Oro";
        if (terminalName.contains("Tagum")) return "Davao del Norte";
        if (terminalName.contains("Panabo")) return "Davao del Norte";
        if (terminalName.contains("Davao City")) return "Davao City";
        if (terminalName.contains("Digos")) return "Davao del Sur";
        return "Davao Region";
    }
}
