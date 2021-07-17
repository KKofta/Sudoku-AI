package sudoku_ai.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EasySolver extends Thread {

    private Tile[][] tilesArray = new Tile[9][9];
    private static List<Set> columnSetList = new ArrayList<>(9);
    private static List<Set> rowSetList = new ArrayList<>(9);
    private static List<Set> squareSetList = new ArrayList<>(9);

    public EasySolver(Tile[][] tilesArray, List<Set> columnSetList, List<Set> rowSetList, List<Set> squareSetList) {
        this.tilesArray = tilesArray;
        this.columnSetList = columnSetList;
        this.rowSetList = rowSetList;
        this.squareSetList = squareSetList;
    }

    @Override
    public void run() {
        solveSudoku(true);
        stop();
    }

    public synchronized void solveSudoku(boolean isAnimation) {
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
                        if (isAnimation) {
                            tilesArray[row][col].setGreenBorderColor();
                            try {
                                wait(100);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(EasySolver.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        //additional set to avoid unintended destruction
                        Set<Integer> resultSet = new HashSet<>(basicSet);
                        //check col
                        resultSet.retainAll(columnSetList.get(col));
                        //check row
                        resultSet.retainAll(rowSetList.get(row));
                        //check square
                        int squareIndex = calculateSquareIndex(row, col);
                        resultSet.retainAll(squareSetList.get(squareIndex));

                        Integer[] resultArray = new Integer[resultSet.size()];
                        resultSet.toArray(resultArray);

                        if (resultSet.size() == 1) {
                            //display the only possible number after intersections
                            //Integer[] resultArray = new Integer[1];
                            //resultSet.toArray(resultArray);
                            int resultNumber = resultArray[0];
                            tilesArray[row][col].setCalculatedNumber(resultNumber);
                            //delete chosen number from sets
                            columnSetList.get(col).remove(resultNumber);
                            rowSetList.get(row).remove(resultNumber);
                            squareSetList.get(squareIndex).remove(resultNumber);
                        } else {
                            int resultNumber = method2(row, col, resultArray);
                            if (resultNumber != 0) {
                                tilesArray[row][col].setCalculatedNumber(resultNumber);
                                //delete chosen number from sets
                                columnSetList.get(col).remove(resultNumber);
                                rowSetList.get(row).remove(resultNumber);
                                squareSetList.get(squareIndex).remove(resultNumber);
                            }
                        }
                        if (isAnimation) {
                            tilesArray[row][col].setWhiteBorderColor();
                        }
                    }
                }
            }
        }
    }

    private int method2(int row, int col, Integer[] array) {

        for (int i = 0; i < array.length; i++) {
            int count = 0;
            //check rows
            if (row == 0 || row == 3 || row == 6) {
                if (!rowSetList.get(row + 1).contains(array[i]) && !rowSetList.get(row + 2).contains(array[i])) {
                    count++;
                }
            } else if (row == 1 || row == 4 || row == 7) {
                if (!rowSetList.get(row - 1).contains(array[i]) && !rowSetList.get(row + 1).contains(array[i])) {
                    count++;
                }
            } else {
                if (!rowSetList.get(row - 1).contains(array[i]) && !rowSetList.get(row - 2).contains(array[i])) {
                    count++;
                }
            }

            //check columns
            if (col == 0 || col == 3 || col == 6) {
                if (!columnSetList.get(col + 1).contains(array[i]) && !columnSetList.get(col + 2).contains(array[i])) {
                    count++;
                }
            } else if (col == 1 || col == 4 || col == 7) {
                if (!columnSetList.get(col - 1).contains(array[i]) && !columnSetList.get(col + 1).contains(array[i])) {
                    count++;
                }
            } else {
                if (!columnSetList.get(col - 1).contains(array[i]) && !columnSetList.get(col - 2).contains(array[i])) {
                    count++;
                }
            }

            if (count == 2) {
                return array[i];
            }
        }
        return 0;
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

    private static boolean isAlgorithmFinished() {

        for (int i = 0; i < 9; i++) {
            if (columnSetList.get(i).isEmpty() == false || rowSetList.get(i).isEmpty() == false || squareSetList.get(i).isEmpty() == false) {
                return false;
            }
        }
        //idk if here is fine
        columnSetList.clear();
        rowSetList.clear();
        squareSetList.clear();
        return true;
    }

}
