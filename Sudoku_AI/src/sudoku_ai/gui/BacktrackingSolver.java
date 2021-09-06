package sudoku_ai.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BacktrackingSolver extends Thread {

    private Tile[][] tilesArray = new Tile[9][9];
    private static List<Set> columnSetList = new ArrayList<>(9);
    private static List<Set> rowSetList = new ArrayList<>(9);
    private static List<Set> squareSetList = new ArrayList<>(9);
    private Set<Integer> basicSet = createBasicSet();

    public BacktrackingSolver(Tile[][] tilesArray, List<Set> columnSetList, List<Set> rowSetList, List<Set> squareSetList) {
        this.tilesArray = tilesArray;
        this.columnSetList = columnSetList;
        this.rowSetList = rowSetList;
        this.squareSetList = squareSetList;
    }

    @Override
    public void run() {
        backtrackingAlgorithm(0, 0);
        stop();
    }

    private synchronized boolean backtrackingAlgorithm(int row, int col) {

        if (isFinished(row, col)) {
            GUI.getBoard().setRows(rowSetList);
            GUI.getBoard().setCols(columnSetList);
            GUI.getBoard().SetSquares(squareSetList);
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
                    columnSetList.get(col).remove(n);
                    rowSetList.get(row).remove(n);
                    squareSetList.get(squareIndex).remove(n);
                    try {
                        wait(40);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BacktrackingSolver.class.getName()).log(Level.SEVERE, null, ex);
                    }

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
