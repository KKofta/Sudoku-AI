package sudoku_ai.gui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class Board extends Pane {

    private final Tile[][] tilesArray = new Tile[9][9];
    private final double tileSize;
    private final double boardSize;

    public Board(double boardSize) {

        this.boardSize = boardSize;
        this.tileSize = this.boardSize / 9;

        createWiderLines();
        createGrid();
    }

    private void createWiderLines() {
        for (int i = 0; i < 10; i = i + 3) {
            Line line = new Line();
            line.setStrokeWidth(3);

            line.setStartX(i * tileSize);
            line.setStartY(0);
            line.setEndX(i * tileSize);
            line.setEndY(boardSize);

            getChildren().addAll(line);
        }

        for (int i = 0; i < 10; i = i + 3) {
            Line line = new Line();
            line.setStrokeWidth(3);

            line.setStartX(0);
            line.setStartY(i * tileSize);
            line.setEndX(boardSize);
            line.setEndY(i * tileSize);

            getChildren().addAll(line);
        }
    }

    private void createGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Tile tile = new Tile(tileSize, i, j);
                tile.setTranslateX(i * tileSize);
                tile.setTranslateY(j * tileSize);

                getChildren().addAll(tile);

                tilesArray[i][j] = tile;
            }
        }
    }

    public void clearBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tilesArray[i][j].setNumber("", i, j);
                //tilesAray[i][j] == 0; clear array?
            }
        }
    }

    public void setNumber(int number, int IDx, int IDy) {
        String numberString = Integer.toString(number);
        tilesArray[IDx][IDy].setNumber(numberString, IDx, IDy);
    }
}
