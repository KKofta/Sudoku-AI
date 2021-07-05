package sudoku_ai.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class Board extends StackPane {

    public Board(double boardSize) {

        double tileSize = boardSize / 9;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Tile tile = new Tile(tileSize);
                tile.setTranslateX(i * tileSize);
                tile.setTranslateY(j * tileSize);
                
                //setAlignment(tile, Pos.TOP_LEFT);
                getChildren().addAll(tile);
            }
        }
        setAlignment(Pos.TOP_LEFT);
    }

}
