package view.components;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.Terminal;

public class MapMarker extends StackPane {
    private Terminal terminal;

    public MapMarker(Terminal terminal, Runnable onClick) {
        this.terminal = terminal;

        ImageView iv = new ImageView(new Image("file:assets/img/busMarker.png"));
        iv.setFitWidth(40);
        iv.setPreserveRatio(true);

        Label name = new Label(terminal.getName());
        name.setStyle("-fx-font-size: 10px; -fx-background-color: rgba(255,255,255,0.8); -fx-padding: 2px;");
        name.setVisible(false);

        setOnMouseEntered(e -> name.setVisible(true));
        setOnMouseExited(e -> name.setVisible(false));

        // Boss Gemina dinhia tung logic kung magclick ka sa marker
        setOnMouseClicked(e -> onClick.run()); 

        getChildren().addAll(iv, name);
    }


    public Terminal getTerminal() { return terminal; }
}
