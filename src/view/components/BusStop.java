package view.components;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BusStop extends StackPane {
    public BusStop(double x, double y) {
        Circle outerCircle = new Circle(20);
        outerCircle.setFill(Color.web("#E74C3C"));
        outerCircle.setStroke(Color.WHITE);
        outerCircle.setStrokeWidth(3);

        Label busIcon = new Label("🚌");
        busIcon.setStyle("-fx-font-size: 18px;");

        getChildren().addAll(outerCircle, busIcon);
        setLayoutX(x);
        setLayoutY(y);
    }
}
