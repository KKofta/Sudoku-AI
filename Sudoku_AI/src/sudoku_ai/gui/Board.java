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
            Line line = new Line(i * tileSize, 0, i * tileSize, boardSize);
            line.setStrokeWidth(3);

            getChildren().addAll(line);
        }

        for (int i = 0; i < 10; i = i + 3) {
            Line line = new Line(0, i * tileSize, boardSize, i * tileSize);
            line.setStrokeWidth(3);

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
                tilesArray[i][j].setNumber(0, i, j);
            }
        }
    }

    public void loadSampleBoard() {
        int[][] testArray = {
            {0, 0, 0, 2, 6, 0, 7, 0, 1},
            {6, 8, 0, 0, 7, 0, 0, 9, 0},
            {1, 9, 0, 0, 0, 4, 5, 0, 0},
            {8, 2, 0, 1, 0, 0, 0, 4, 0},
            {0, 0, 4, 6, 0, 2, 9, 0, 0},
            {0, 5, 0, 0, 0, 3, 0, 2, 8},
            {0, 0, 9, 3, 0, 0, 0, 7, 4},
            {0, 4, 0, 0, 5, 0, 0, 3, 6},
            {7, 0, 3, 0, 1, 8, 0, 0, 0}};

        for (int i = 0; i < 9; i++) { //I messed up indexes, because on GUI I was thinking as x,y - reversely to arrays
            for (int j = 0; j < 9; j++) {
                int number = testArray[j][i]; //change [i][j] to [j][i] for now
                tilesArray[i][j].setNumber(number, i, j);
            }
        }
    }

    public void setNumber(int number, int IDx, int IDy) {
        tilesArray[IDx][IDy].setNumber(number, IDx, IDy);
    }
}
