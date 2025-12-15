package view.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Terminal;
import model.TerminalEvent;

public class TerminalEventPanel extends VBox {

    private Runnable onClose;
    private static final double ICON_SIZE = 60;

    public TerminalEventPanel(Terminal terminal, TerminalEvent event) {

        setAlignment(Pos.CENTER);
        setSpacing(12);
        setPrefWidth(300);
        setMaxHeight(320);
        setMaxWidth(300);
        setStyle("""
            -fx-background-color: white;
            -fx-border-color: #cccccc;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-padding: 20;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 10, 0, 0, 4);
        """);

        Image img = new Image("file:" + event.getIconPath());
        ImageView icon = new ImageView(img);

        icon.setFitWidth(ICON_SIZE);
        icon.setFitHeight(ICON_SIZE);
        icon.setSmooth(true);
        icon.setPreserveRatio(true); 

        Label terminalLabel = new Label(terminal.getName());
        terminalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label titleLabel = new Label(event.getTitle());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#1a5490"));

        Label messageLabel = new Label(event.getMessage());
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(Pos.CENTER);

        Button okBtn = new Button("OK");
        okBtn.setPrefWidth(100);
        okBtn.setOnAction(e -> {
            if (onClose != null) onClose.run();
        });

        getChildren().addAll(
            icon,
            terminalLabel,
            titleLabel,
            messageLabel,
            okBtn
        );
    }

    public void setOnClose(Runnable onClose) {
        this.onClose = onClose;
    }
}
