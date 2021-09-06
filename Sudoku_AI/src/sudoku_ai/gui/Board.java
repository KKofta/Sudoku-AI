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
    private static List<Set> columnSetList = new ArrayList<>(9);
    private static List<Set> rowSetList = new ArrayList<>(9);
    private static List<Set> squareSetList = new ArrayList<>(9);
    private final double boardSize = 500;
    private final double tileSize = boardSize / 9;
    private EasySolver easySolver = new EasySolver(tilesArray, columnSetList, rowSetList, squareSetList);
    private BacktrackingSolver backtrackingSolver = new BacktrackingSolver(tilesArray, columnSetList, rowSetList, squareSetList);
    private BoardGenerator boardGenerator = new BoardGenerator(tilesArray, columnSetList, rowSetList, squareSetList, 45, false);
    private boolean assistance = false;

    public Board() {
        createWiderLines();
        createGrid();
        initializeLists();
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
                Tile tile = new Tile(tileSize);
                tile.setTranslateX(j * tileSize);
                tile.setTranslateY(i * tileSize);

                getChildren().addAll(tile);

                tilesArray[i][j] = tile;
                tile.setRow(i);
                tile.setColumn(j);
            }
        }
    }

    public void clearBoard() {
        initializeLists();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tilesArray[i][j].setNumber(0);
                tilesArray[i][j].setWhiteBorderColor();
                tilesArray[i][j].turnOffTile();
            }
        }
        boardGenerator.stop();
        easySolver.stop();
        backtrackingSolver.stop();
    }

    public void loadEasyBoard() {
        clearBoard();
        initializeLists();

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
                tilesArray[row][col].setNumber(number);
                removeFromSets(row, col, number);
            }
        }
    }

    public void loadIntermediateBoard() {
        clearBoard();
        initializeLists();

        int[][] sampleArray = {
            {0, 2, 0, 6, 0, 8, 0, 0, 0},
            {5, 8, 0, 0, 0, 9, 7, 0, 0},
            {0, 0, 0, 0, 4, 0, 0, 0, 0},
            {3, 7, 0, 0, 0, 0, 5, 0, 0},
            {6, 0, 0, 0, 0, 0, 0, 0, 4},
            {0, 0, 8, 0, 0, 0, 0, 1, 3},
            {0, 0, 0, 0, 2, 0, 0, 0, 0},
            {0, 0, 9, 8, 0, 0, 0, 3, 6},
            {0, 0, 0, 3, 0, 6, 0, 9, 0}};

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int number = sampleArray[row][col];
                tilesArray[row][col].setNumber(number);
                removeFromSets(row, col, number);
            }
        }
    }

    public void loadHardBoard() {
        clearBoard();
        initializeLists();

        int[][] sampleArray = {
            {0, 0, 0, 0, 0, 0, 6, 8, 0},
            {0, 0, 0, 0, 7, 3, 0, 0, 9},
            {3, 0, 9, 0, 0, 0, 0, 4, 5},
            {4, 9, 0, 0, 0, 0, 0, 0, 0},
            {8, 0, 3, 0, 5, 0, 9, 0, 2},
            {0, 0, 0, 0, 0, 0, 0, 3, 6},
            {9, 6, 0, 0, 0, 0, 3, 0, 8},
            {7, 0, 0, 6, 8, 0, 0, 0, 0},
            {0, 2, 8, 0, 0, 0, 0, 0, 0}};

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int number = sampleArray[row][col];
                tilesArray[row][col].setNumber(number);
                removeFromSets(row, col, number);
            }
        }
    }

    public void loadNotFunBoard() {
        clearBoard();
        initializeLists();

        int[][] sampleArray = {
            {0, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 6, 0, 0, 0, 0, 3},
            {0, 7, 4, 0, 8, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 0, 2},
            {0, 8, 0, 0, 4, 0, 0, 1, 0},
            {6, 0, 0, 5, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 7, 8, 0},
            {5, 0, 0, 0, 0, 9, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 4, 0}};

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int number = sampleArray[row][col];
                tilesArray[row][col].setNumber(number);
                removeFromSets(row, col, number);
            }
        }
    }

    public void animateGenerator(int amountToRemove, boolean experimentMode) {
        clearBoard();
        boardGenerator = new BoardGenerator(tilesArray, columnSetList, rowSetList, squareSetList, amountToRemove, experimentMode);
        boardGenerator.start();
    }

    public void stopAnimationGenerator() {
        boardGenerator.stop();
    }

    public void animateEasySudoku() {
        easySolver = new EasySolver(tilesArray, columnSetList, rowSetList, squareSetList);
        easySolver.start();
    }

    public void animateBacktrackingSudoku() {
        backtrackingSolver = new BacktrackingSolver(tilesArray, columnSetList, rowSetList, squareSetList);
        backtrackingSolver.start();
    }

    public void stopAnimationSolver() {
        easySolver.stop();
        backtrackingSolver.stop();
    }

    private void initializeLists() {
        columnSetList.clear();
        rowSetList.clear();
        squareSetList.clear();

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
        switch (col) {
            case 0:
            case 1:
            case 2:
                squareIndex = 3 * x;
                break;
            case 3:
            case 4:
            case 5:
                squareIndex = 3 * x + 1;
                break;
            case 6:
            case 7:
            case 8:
                squareIndex = 3 * x + 2;
                break;
            default:
                break;
        }
        return squareIndex;
    }

    public void setNumber(int number, int IDx, int IDy) {
        tilesArray[IDx][IDy].setNumber(number);
    }

    public void lightRowsColsSquare(Tile tile) {
        //clear light of previous click
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tilesArray[i][j].turnOffTile();
            }
        }

        //assistance (lighting)
        int row = tile.getRow();
        int col = tile.getColumn();

        int squareRowMin;
        int squareRowMax;
        int squareColMin;
        int squareColMax;
        if (row < 3) {
            squareRowMin = 0;
            squareRowMax = 3;
        } else if (row > 5) {
            squareRowMin = 6;
            squareRowMax = 9;
        } else {
            squareRowMin = 3;
            squareRowMax = 6;
        }

        if (col < 3) {
            squareColMin = 0;
            squareColMax = 3;
        } else if (col > 5) {
            squareColMin = 6;
            squareColMax = 9;
        } else {
            squareColMin = 3;
            squareColMax = 6;
        }

        if (assistance) {
            //light row & col
            for (int i = 0; i < 9; i++) {
                tilesArray[row][i].lightTile();
                tilesArray[i][col].lightTile();
            }

            //light square
            for (int i = squareRowMin; i < squareRowMax; i++) {
                for (int j = squareColMin; j < squareColMax; j++) {
                    tilesArray[i][j].lightTile();
                }
            }
        }

        tilesArray[row][col].lightMainTile();

    }

    public void turnOffRowsColsSquare() {
        //clear lighted tiles after second click
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tilesArray[i][j].turnOffTile();
            }
        }
    }

    public void tilesActionOn() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tilesArray[i][j].setTileActionOn();
            }
        }
    }

    public void tilesActionOff() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tilesArray[i][j].setTileActionOff();
            }
        }
    }

    public void setAssistance(boolean assistance) {
        this.assistance = assistance;
    }

    public void removeFromSets(int row, int col, int number) {
        columnSetList.get(col).remove(number);
        rowSetList.get(row).remove(number);
        int squareIndex = calculateSquareIndex(row, col);
        squareSetList.get(squareIndex).remove(number);
    }

    public void addToSets(int row, int col, int number) {
        columnSetList.get(col).add(number);
        rowSetList.get(row).add(number);
        int squareIndex = calculateSquareIndex(row, col);
        squareSetList.get(squareIndex).add(number);
    }

    public boolean isBoardValid() {
        for (int i = 0; i < 9; i++) {
            if (!columnSetList.get(i).isEmpty() || !rowSetList.get(i).isEmpty() || !squareSetList.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void saveBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tilesArray[i][j].changeYellowToWhite();
            }
        }
    }

    public void saveGeneratedBoard() {
        saveBoard();
        rowSetList = boardGenerator.getRowSets();
        columnSetList = boardGenerator.getColSets();
        squareSetList = boardGenerator.getSquareSets();
    }
}
