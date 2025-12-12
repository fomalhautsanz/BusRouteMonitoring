package view.components;

import javafx.scene.control.Label;

public class CityLabel extends Label {
    public CityLabel(String text, double x, double y) {
        super(text);
        setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        setLayoutX(x);
        setLayoutY(y);
    }
}
