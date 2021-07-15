package sudoku_ai.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Board extends Pane {

    private final Tile[][] tilesArray = new Tile[9][9];
    private static List<Set> columnSetList = new ArrayList<>(9);//static?
    private static List<Set> rowSetList = new ArrayList<>(9);//static?
    private static List<Set> squareSetList = new ArrayList<>(9);//static?
    private final double tileSize;
    private final double boardSize;

    public Board(double boardSize) {

        this.boardSize = boardSize;
        this.tileSize = this.boardSize / 9;

        createWiderLines();
        createGrid();
        initializeLists(columnSetList, rowSetList, squareSetList);//idk if it's fine here
    }

    private void createWiderLines() {
        for (int i = 0; i < 10; i = i + 3) {
            Line line = new Line(i * tileSize, 0, i * tileSize, boardSize);
            line.setStrokeWidth(4);
            line.setStroke(Color.WHITESMOKE);

            getChildren().addAll(line);
        }

        for (int i = 0; i < 10; i = i + 3) {
            Line line = new Line(0, i * tileSize, boardSize, i * tileSize);
            line.setStrokeWidth(4);
            line.setStroke(Color.WHITESMOKE);

            getChildren().addAll(line);
        }
    }

    private void createGrid() {
        //we look at grid coordinates not as (X,Y), but as in 2D array
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Tile tile = new Tile(tileSize/*, i, j*/);
                tile.setTranslateX(j * tileSize);
                tile.setTranslateY(i * tileSize);

                getChildren().addAll(tile);

                tilesArray[i][j] = tile;
            }
        }
    }

    public void clearBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tilesArray[i][j].setNumber(0/*, i, j*/);
            }
        }
    }

    public void loadSampleBoard() {
        int[][] sampleArray = {
            {0, 0, 0, 2, 6, 0, 7, 0, 1},
            {6, 8, 0, 0, 7, 0, 0, 9, 0},
            {1, 9, 0, 0, 0, 4, 5, 0, 0},
            {8, 2, 0, 1, 0, 0, 0, 4, 0},
            {0, 0, 4, 6, 0, 2, 9, 0, 0},
            {0, 5, 0, 0, 0, 3, 0, 2, 8},
            {0, 0, 9, 3, 0, 0, 0, 7, 4},
            {0, 4, 0, 0, 5, 0, 0, 3, 6},
            {7, 0, 3, 0, 1, 8, 0, 0, 0}};

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int number = sampleArray[row][col];
                tilesArray[row][col].setNumber(number/*, i, j*/);
                columnSetList.get(col).remove(number);
                rowSetList.get(row).remove(number);
                int squareIndex = calculateSquareIndex(row, col);
                squareSetList.get(squareIndex).remove(number);
            }
        }
    }

    public void solveSudoku() {
        /*Algorithm: 
        We loop through columns and rows and look for empty tiles. 
        In empty tiles we calculate intersection of set of rows, set of columns and set of squares.
        If the resulting set contains only one number it means that it's the only possible number to enter.
         */
        Set<Integer> basicSet = new HashSet<>();

        for (int i = 1; i <= 9; i++) {
            basicSet.add(i);
        }

        while (isAlgorithmFinished() == false) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (tilesArray[row][col].getNumber() == 0) {
                        //additional set to avoid unintended destruction
                        Set<Integer> resultSet = new HashSet<>(basicSet);
                        //check col
                        resultSet.retainAll(columnSetList.get(col));
                        //check row
                        resultSet.retainAll(rowSetList.get(row));
                        //check square
                        int squareIndex = calculateSquareIndex(row, col);
                        resultSet.retainAll(squareSetList.get(squareIndex));

                        if (resultSet.size() == 1) {
                            //display the only possible number after intersections
                            Integer[] resultArray = new Integer[1];
                            resultSet.toArray(resultArray);
                            int resultNumber = resultArray[0];
                            tilesArray[row][col].setCalculatedNumber(resultNumber);
                            //delete chosen number from sets
                            columnSetList.get(col).remove(resultNumber);
                            rowSetList.get(row).remove(resultNumber);
                            squareSetList.get(squareIndex).remove(resultNumber);
                        }
                    }
                }
            }
        }
    }

    private static void initializeLists(List<Set> columnSetList, List<Set> rowSetList, List<Set> squareSetList) {

        Set<Integer> basicSet = new HashSet<>();

        for (int i = 1; i <= 9; i++) {
            basicSet.add(i);
        }

        for (int i = 0; i < 9; i++) {
            columnSetList.add(new HashSet<>(basicSet));
            rowSetList.add(new HashSet<>(basicSet));
            squareSetList.add(new HashSet<>(basicSet));
        }
    }

    private int calculateSquareIndex(int row, int col) {
        int squareIndex = 0;
        int x = row / 3;
        if (col == 0 || col == 1 || col == 2) {
            squareIndex = 3 * x;
        } else if (col == 3 || col == 4 || col == 5) {
            squareIndex = 3 * x + 1;
        } else if (col == 6 || col == 7 || col == 8) {
            squareIndex = 3 * x + 2;
        }
        return squareIndex;
    }

    private static boolean isAlgorithmFinished() {

        for (int i = 0; i < 9; i++) {
            if (columnSetList.get(i).isEmpty() == false || rowSetList.get(i).isEmpty() == false || squareSetList.get(i).isEmpty() == false) {
                return false;
            }
        }
        return true;
    }

    public void setNumber(int number, int IDx, int IDy) {
        tilesArray[IDx][IDy].setNumber(number/*, IDx, IDy*/);
    }

}
