package view.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Terminal;

import java.io.File;

public class MapMarker extends VBox {

    private static final double PIN_WIDTH = 24;
    private static final double PIN_HEIGHT = 36;
    private static final double OFFSET_DOWN = 4; 

    private final Terminal terminal;

    public MapMarker(Terminal terminal, Runnable onClick) {
        this.terminal = terminal;

        ImageView pin = new ImageView(
                new Image("file:assets/img/busMarker.png")
        );

        pin.setFitWidth(PIN_WIDTH);
        pin.setFitHeight(PIN_HEIGHT);
        pin.setPreserveRatio(true);

        getChildren().add(pin);

        // 🔥 IMPORTANT: anchor correction
        setTranslateX(-PIN_WIDTH / 2);
        setTranslateY(-PIN_HEIGHT + OFFSET_DOWN);

        setPickOnBounds(false);
        setOnMouseClicked(e -> onClick.run());
    }

    public Terminal getTerminal() {
        return terminal;
    }
}

