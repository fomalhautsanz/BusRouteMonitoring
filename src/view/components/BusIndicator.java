package view.components;

import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class BusIndicator extends StackPane {

    public BusIndicator() {
        Circle circle = new Circle(15);
        circle.setFill(Color.web("#FFA500"));
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(2);

        Label icon = new Label("🚌");
        icon.setStyle("-fx-font-size: 16px;");

        getChildren().addAll(circle, icon);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(3), this);
        transition.setByX(150);
        transition.setByY(-100);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }
}
