package sudoku_ai.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solver {

    private Tile[][] tilesArray = new Tile[9][9];
    private static List<Set> columnSetList = new ArrayList<>(9);
    private static List<Set> rowSetList = new ArrayList<>(9);
    private static List<Set> squareSetList = new ArrayList<>(9);

    public Solver(Tile[][] tilesArray, List<Set> columnSetList, List<Set> rowSetList, List<Set> squareSetList) {
        this.tilesArray = tilesArray;
        this.columnSetList = columnSetList;
        this.rowSetList = rowSetList;
        this.squareSetList = squareSetList;
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
                if (rowSetList.get(row + 1).contains(array[i]) && rowSetList.get(row + 2).contains(array[i])) {
                    count++;
                }
            } else if (row == 1 || row == 4 || row == 7) {
                if (rowSetList.get(row - 1).contains(array[i]) && rowSetList.get(row + 1).contains(array[i])) {
                    count++;
                }
            } else {
                if (rowSetList.get(row - 1).contains(array[i]) && rowSetList.get(row - 2).contains(array[i])) {
                    count++;
                }
            }

            //check columns
            if (col == 0 || col == 3 || col == 6) {
                if (columnSetList.get(col + 1).contains(array[i]) && columnSetList.get(col + 2).contains(array[i])) {
                    count++;
                }
            } else if (col == 1 || col == 4 || col == 7) {
                if (columnSetList.get(col - 1).contains(array[i]) && columnSetList.get(col + 1).contains(array[i])) {
                    count++;
                }
            } else {
                if (columnSetList.get(col - 1).contains(array[i]) && columnSetList.get(col - 2).contains(array[i])) {
                    count++;
                }
            }

            if (count == 2) {
                return array[i];
            }
        }
        return 0;
    }

    public void solveSudokuNaive() {
        naiveAlgorithm(0, 0);
    }

    private boolean naiveAlgorithm(int row, int col) {

        if (isFinished(row, col)) {
            return true;
        }

        if (col == 9) {
            row++;
            col = 0;
        }

        if (tilesArray[row][col].getNumber() != 0) {
            naiveAlgorithm(row, col + 1);
        }

        for (int n = 1; n <= 9; n++) {

            int squareIndex = calculateSquareIndex(row, col);

            if (isPossible(row, col, n)) {
                tilesArray[row][col].setCalculatedNumber(n);

                columnSetList.get(col).remove(n);
                rowSetList.get(row).remove(n);
                squareSetList.get(squareIndex).remove(n);

                if (naiveAlgorithm(row, col + 1)) {
                    return true;
                }
            }

            tilesArray[row][col].setCalculatedNumber(0);

            columnSetList.get(col).add(n);
            rowSetList.get(row).add(n);
            squareSetList.get(squareIndex).add(n);
        }
        return false;
    }

    private boolean isFinished(int row, int col) {
        return row == 8 && col == 9;
    }

    private boolean isPossible(int row, int col, int number) {
        Set<Integer> basicSet = new HashSet<>();

        for (int i = 1; i <= 9; i++) {
            basicSet.add(i);
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
