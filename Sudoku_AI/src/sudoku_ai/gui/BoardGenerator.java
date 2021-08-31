package sudoku_ai.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardGenerator extends Thread {

    private Tile[][] tilesArray = new Tile[9][9];
    private static List<Set> columnSetList = new ArrayList<>(9);
    private static List<Set> rowSetList = new ArrayList<>(9);
    private static List<Set> squareSetList = new ArrayList<>(9);
    Set<Integer> basicSet = createBasicSet();

    public BoardGenerator(Tile[][] tilesArray, List<Set> columnSetList, List<Set> rowSetList, List<Set> squareSetList) {
        this.tilesArray = tilesArray;
        this.columnSetList = columnSetList;
        this.rowSetList = rowSetList;
        this.squareSetList = squareSetList;
    }

    @Override
    public void run() {
        generateBoard();
        stop();
    }

    private synchronized void generateBoard() {

        //Generation of diagonal squares
        int loop = 0;

        while (loop < 3) {
            int squareNum = loop * 3;
            for (int i = 0 + squareNum; i < 3 + squareNum; i++) {
                for (int j = 0 + squareNum; j < 3 + squareNum; j++) {
                    Set<Integer> resultSet = new HashSet<>(squareSetList.get(loop * 4));

                    Integer[] resultArray = new Integer[resultSet.size()];
                    resultSet.toArray(resultArray);

                    Random r = new Random();
                    int nextRandomNumberIndex = r.nextInt(resultArray.length);
                    int resultNumber = resultArray[nextRandomNumberIndex];

                    tilesArray[i][j].setCalculatedNumber(resultNumber);

                    try {
                        wait(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BacktrackingSolver.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    rowSetList.get(i).remove(resultNumber);
                    columnSetList.get(j).remove(resultNumber);
                    squareSetList.get(loop * 4).remove(resultNumber);
                }
            }
            loop++;
        }
        backtrackingAlgorithm(0, 0);
    }

    private synchronized boolean backtrackingAlgorithm(int row, int col) {

        if (isFinished(row, col)) {
            return true;
        }

        if (col == 9) {
            row++;
            col = 0;
        }

        if (tilesArray[row][col].getNumber() != 0) {
            if (backtrackingAlgorithm(row, col + 1)) {
                return true;
            }
        } else {

            for (int n = 1; n <= 9; n++) {

                int squareIndex = calculateSquareIndex(row, col);

                if (isPossible(row, col, squareIndex, n)) {
                    tilesArray[row][col].setCalculatedNumber(n);
                    try {
                        wait(40);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BacktrackingSolver.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    columnSetList.get(col).remove(n);
                    rowSetList.get(row).remove(n);
                    squareSetList.get(squareIndex).remove(n);

                    if (backtrackingAlgorithm(row, col + 1)) {
                        return true;
                    } else {
                        tilesArray[row][col].setCalculatedNumber(0);

                        columnSetList.get(col).add(n);
                        rowSetList.get(row).add(n);
                        squareSetList.get(squareIndex).add(n);
                    }
                }

            }
        }
        return false;
    }

    private boolean isFinished(int row, int col) {
        return row == 8 && col == 9;
    }

    private boolean isPossible(int row, int col, int squareIndex, int number) {
        //additional set to avoid unintended destruction
        Set<Integer> resultSet = new HashSet<>(basicSet);
        //check col
        resultSet.retainAll(columnSetList.get(col));
        //check row
        resultSet.retainAll(rowSetList.get(row));
        //check square
        resultSet.retainAll(squareSetList.get(squareIndex));
        //resultSet contains possible numbers to enter
        return resultSet.contains(number);
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

    private Set<Integer> createBasicSet() {
        basicSet = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            basicSet.add(i);
        }
        return basicSet;
    }

}
