package view.components;

import javafx.scene.control.Button;

public class ControlButton extends Button {
    public ControlButton(String text) {
        super(text);
        setPrefSize(50, 50);
        setStyle("-fx-background-color: #1a5490; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 25; -fx-cursor: hand;");

        setOnMouseEntered(e -> setStyle("-fx-background-color: #2d6bb8; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 25; -fx-cursor: hand;"));
        setOnMouseExited(e -> setStyle("-fx-background-color: #1a5490; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 25; -fx-cursor: hand;"));
    }
}
