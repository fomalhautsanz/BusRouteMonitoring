package view.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.nio.file.Paths;

import App.MainApp;

public class AboutUI {

    private Image loadImage(String path) {
        return new Image(Paths.get(path).toUri().toString());
    }

    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        Region mapBackground = new Region();
        mapBackground.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b8d4e8 0%, #d4c4a8 100%);"
        );

        Pane mapOverlay = new Pane();
        mapOverlay.setStyle("-fx-background-color: rgba(200, 213, 220, 0.3);");

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        mainLayout.setTop(createTopBar(primaryStage));
        mainLayout.setCenter(createCards());

        root.getChildren().addAll(mapBackground, mapOverlay, mainLayout);

        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setTitle("Bus Route Monitoring System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTopBar(Stage stage) {
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        StackPane backButton = new StackPane();
        backButton.setPrefSize(50, 50);
        backButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        SVGPath backArrow = new SVGPath();
        backArrow.setContent("M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z");
        backArrow.setFill(Color.BLACK);
        backArrow.setScaleX(2.5);
        backArrow.setScaleY(2.5);

        backButton.getChildren().add(backArrow);

        backButton.setOnMouseClicked(e -> {
            try {
                stage.close();
                MainApp mainApp = new MainApp();
                Stage mainStage = new Stage();
                mainApp.start(mainStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox titleBox = new VBox(-3);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().addAll(
                createTitleLabel("BUS ROUTE"),
                createTitleLabel("MONITORING SYSTEM")
        );

        topBar.getChildren().addAll(backButton, spacer, titleBox);
        return topBar;
    }

    private Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        label.setTextFill(Color.WHITE);
        label.setStyle(
                "-fx-background-color:#2B5E9E;" +
                "-fx-padding:3 15;" +
                "-fx-background-radius:3;"
        );
        return label;
    }
    
    private HBox createCards() {
        HBox cards = new HBox(25);
        cards.setAlignment(Pos.CENTER);
        cards.setPadding(new Insets(50, 30, 30, 30));

        cards.getChildren().addAll(
                createInfoCard(
                        "SYSTEM",
                        "assets/img/system.jpg",
                        "The Bus Route Monitoring System is a\nproject designed to track and monitor\nbuses in real time for improved\ntransportation management."
                ),
                createInfoCard(
                        "PURPOSE",
                        "assets/img/Purpose.png",
                        "This system ensures buses follow routes,\navoid delays, and provide accurate\ninformation to passengers."
                ),
                createInfoCard(
                        "FEATURES",
                        "assets/img/feature.jpg",
                        "Real-time tracking, route monitoring,\ndelay alerts, and operator dashboards."
                )
        );

        return cards;
    }

    private VBox createInfoCard(String title, String imagePath, String description) {
        VBox card = new VBox(20);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(30));
        card.setPrefSize(350, 500);

        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-background-radius:30;" +
                "-fx-border-color:#1E4D8B;" +
                "-fx-border-width:5;" +
                "-fx-border-radius:30;" +
                "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.2),10,0,0,5);"
        );

        ImageView imageView = new ImageView(loadImage(imagePath));
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#1E4D8B"));

        Label desc = new Label(description);
        desc.setFont(Font.font("Arial", 14));
        desc.setWrapText(true);
        desc.setMaxWidth(300);
        desc.setAlignment(Pos.CENTER);
        desc.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        card.getChildren().addAll(imageView, titleLabel, desc);
        return card;
    }
}
