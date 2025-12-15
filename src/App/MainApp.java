package App;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.nio.file.Paths;

import view.BusRouteMonitoringSystem;
import view.components.AboutUI;

public class MainApp extends Application {

    private Image loadImage(String path) {
        return new Image(Paths.get(path).toUri().toString());
    }

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        ImageView bg = new ImageView(loadImage("assets/img/ma_bg.png"));
        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());

        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(stage.widthProperty());
        overlay.heightProperty().bind(stage.heightProperty());
        overlay.setFill(Color.rgb(0, 0, 0, 0.6));

        StackPane bgLayer = new StackPane(bg, overlay);

        Text t1 = new Text("BUS ROUTE");
        Text t2 = new Text("MONITORING SYSTEM");
        t1.setFill(Color.WHITE);
        t2.setFill(Color.WHITE);

        t1.fontProperty().bind(Bindings.createObjectBinding(
                () -> Font.font("Arial", FontWeight.EXTRA_BOLD, stage.getWidth() * 0.055),
                stage.widthProperty()
        ));
        t2.fontProperty().bind(Bindings.createObjectBinding(
                () -> Font.font("Arial", FontWeight.EXTRA_BOLD, stage.getWidth() * 0.055),
                stage.widthProperty()
        ));

        VBox titleBox = new VBox(5, t1, t2);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(40, 0, 0, 0));
        root.setTop(titleBox);

        ImageView map = new ImageView(loadImage("assets/img/bus_map.png"));
        map.fitWidthProperty().bind(stage.widthProperty().multiply(0.25));
        map.setPreserveRatio(true);

        ImageView marker = new ImageView(loadImage("assets/img/markers.png"));
        marker.fitWidthProperty().bind(stage.widthProperty().multiply(0.06));
        marker.setPreserveRatio(true);

        VBox center = new VBox(10, map, marker);
        center.setAlignment(Pos.CENTER);
        root.setCenter(center);

        VBox buttons = new VBox(25,
                makeButton("START", stage),
                makeButton("ABOUT", stage),
                makeButton("EXIT", stage)
        );
        buttons.setAlignment(Pos.BOTTOM_LEFT);
        buttons.setPadding(new Insets(0, 0, 40, 50));
        root.setLeft(buttons);

        ImageView bubble = new ImageView(loadImage("assets/img/bubble_msg.png"));
        bubble.fitWidthProperty().bind(stage.widthProperty().multiply(0.26));
        bubble.setPreserveRatio(true);

        ImageView driver = new ImageView(loadImage("assets/img/bus_driver.png"));
        driver.fitWidthProperty().bind(stage.widthProperty().multiply(0.32));
        driver.setPreserveRatio(true);

        VBox right = new VBox(5, bubble, driver);
        right.setAlignment(Pos.BOTTOM_RIGHT);
        right.setPadding(new Insets(0, 40, 40, 0));
        root.setRight(right);

        Scene scene = new Scene(new StackPane(bgLayer, root), 1000, 650);
        stage.setScene(scene);
        stage.setTitle("Bus Route Monitoring System");
        stage.show();
    }

    private Button makeButton(String text, Stage stage) {
        Button b = new Button(text);

        b.prefWidthProperty().bind(stage.widthProperty().multiply(0.35));
        b.prefHeightProperty().bind(stage.heightProperty().multiply(0.09));

        b.fontProperty().bind(Bindings.createObjectBinding(
                () -> Font.font("Arial", FontWeight.BOLD, stage.getWidth() * 0.03),
                stage.widthProperty()
        ));

        b.setStyle("-fx-background-color:#1976D2;-fx-text-fill:white;-fx-background-radius:20;");

        switch (text) {
            case "START" -> b.setOnAction(e -> openSystem(stage));
            case "ABOUT" -> b.setOnAction(e -> openAbout());
            case "EXIT"  -> b.setOnAction(e -> stage.close());
        }

        return b;
    }

    private void openSystem(Stage menuStage) {
        try {
            BusRouteMonitoringSystem app = new BusRouteMonitoringSystem();
            Stage stage = new Stage();
            app.start(stage);
            menuStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAbout() {
        try {
            AboutUI about = new AboutUI();
            Stage stage = new Stage();
            about.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
