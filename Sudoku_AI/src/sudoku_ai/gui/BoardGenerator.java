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
    List<Set> columnSetListCopy = new ArrayList<>(9);
    List<Set> rowSetListCopy = new ArrayList<>(9);
    List<Set> squareSetListCopy = new ArrayList<>(9);
    Set<Integer> basicSet = createBasicSet();
    private int amountToRemove;
    private int numberOfSolutions = 0;
    int[][] localBoard = new int[9][9];
    private List rowsColsChecked = new ArrayList<>();
    private boolean experimentMode = false;

    public BoardGenerator(Tile[][] tilesArray, List<Set> columnSetList, List<Set> rowSetList, 
            List<Set> squareSetList, int amountToRemove, boolean experimentMode) {
        this.tilesArray = tilesArray;
        this.columnSetList = columnSetList;
        this.rowSetList = rowSetList;
        this.squareSetList = squareSetList;
        this.amountToRemove = amountToRemove;
        this.experimentMode = experimentMode;
    }

    @Override
    public void run() {
        generateBoard();
        stop();
    }

    private void generateBoard() {
        fillDiagonalSquares();
        fillRemainingTiles();
        removeNumbers(amountToRemove);
    }

    private synchronized void fillDiagonalSquares() {
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
                        wait(40);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BacktrackingSolver.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    removeFromSets(i, j, loop * 4, resultNumber);
                }
            }
            loop++;
        }
    }

    private void fillRemainingTiles() {
        backtrackingAlgorithm(0, 0);
    }

    private synchronized boolean backtrackingAlgorithm(int row, int col) {

        if (isBacktrackingFinished(row, col)) {
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
                    removeFromSets(row, col, squareIndex, n);
                    try {
                        wait(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BacktrackingSolver.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (backtrackingAlgorithm(row, col + 1)) {
                        return true;
                    } else {
                        tilesArray[row][col].setCalculatedNumber(0);
                        addToSets(row, col, squareIndex, n);
                    }
                }

            }
        }
        return false;
    }

    private boolean isBacktrackingFinished(int row, int col) {
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

    private synchronized void removeNumbers(int amountToRemove) {
        initializeCopySets();//needed to calculate solvability outside of gui
        while (amountToRemove > 0 && rowsColsChecked.size() < 81) {
            Random randRow = new Random();
            int randRowIndex = randRow.nextInt(9);
            Random randCol = new Random();
            int randColIndex = randCol.nextInt(9);

            int selectedNumber = tilesArray[randRowIndex][randColIndex].getNumber();

            if (selectedNumber != 0 && wasNotChecked(randRowIndex, randColIndex)) {
                int squareIndex = calculateSquareIndex(randRowIndex, randColIndex);

                addToSets(randRowIndex, randColIndex, squareIndex, selectedNumber);
                //in isSolvable we operate on main sets so we have to remember them from before checking 
                copySets();

                amountToRemove--;
                tilesArray[randRowIndex][randColIndex].setCalculatedNumber(0);
                try {
                    wait(40);
                } catch (InterruptedException ex) {
                    Logger.getLogger(BacktrackingSolver.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (!isSolvable()) {
                    //we copy back remembered sets after isSolvable and remove added number
                    copySetsBack();
                    removeFromSets(randRowIndex, randColIndex, squareIndex, selectedNumber);
                    amountToRemove++;
                    tilesArray[randRowIndex][randColIndex].setCalculatedNumber(selectedNumber);
                    try {
                        wait(40);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BacktrackingSolver.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //we copy back remembered sets after isSolvable
                    copySetsBack();
                }
            }
        }
    }
    
    private boolean isSolvable(){
        if( experimentMode ){
            return isSolvableByBacktracking();
        } else {
            return isSolvableByBasicAlgorithm();
        }
    }

    private boolean isSolvableByBasicAlgorithm() {
        /*We loop through columns and rows and look for empty tiles. 
        In empty tiles we calculate intersection of set of rows, set of columns and set of squares.
        If the resulting set contains only one number it means that it's the only possible number to enter.
        If it is the only possible to enter it means sudoku board is still valid */
        representBoard();//copy of a gui board to calculate in the backend
        int countNumPut;
        while (isEasyAlgFinished() == false) {
            countNumPut = 0;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (localBoard[row][col] == 0) {
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
                            int resultNumber = resultArray[0];
                            localBoard[row][col] = resultNumber;
                            //delete chosen number from sets
                            removeFromSets(row, col, squareIndex, resultNumber);
                            countNumPut++;
                        } else {
                            int resultNumber = method2(row, col, resultArray);
                            if (resultNumber != 0) {
                                localBoard[row][col] = resultNumber;
                                //delete chosen number from sets
                                removeFromSets(row, col, squareIndex, resultNumber);
                                countNumPut++;
                            }
                        }
                    }
                }
            }
            if (countNumPut == 0) {
                return false;
            }
        }
        return true;
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

    private boolean isEasyAlgFinished() {

        for (int i = 0; i < 9; i++) {
            if (!columnSetList.get(i).isEmpty() || !rowSetList.get(i).isEmpty() || !squareSetList.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean isSolvableByBacktracking() {
        numberOfSolutions = 0;
        representBoard();
        boolean areThereTwoSolutions = backtrackingAlgorithmOnCopy(0, 0);

        return !areThereTwoSolutions;
    }

    /*A little modified backtracking algorithm to work in the backend.
    It also checks if the number of board solutions is greater than 1.
    If it is the board is not valid (solvable)*/
    private boolean backtrackingAlgorithmOnCopy(int row, int col) {

        if (isBacktrackingFinished(row, col)) {
            if (numberOfSolutions == 0) {
                numberOfSolutions++;
                return false;
            } else {
                return true;
            }
        }

        if (col == 9) {
            row++;
            col = 0;
        }

        if (localBoard[row][col] != 0) {
            if (backtrackingAlgorithmOnCopy(row, col + 1)) {
                return true;
            }
        } else {
            for (int n = 1; n <= 9; n++) {
                int squareIndex = calculateSquareIndex(row, col);

                if (isPossible(row, col, squareIndex, n)) {
                    localBoard[row][col] = n;
                    removeFromSets(row, col, squareIndex, n);

                    if (backtrackingAlgorithmOnCopy(row, col + 1)) {
                        return true;
                    } else {
                        localBoard[row][col] = 0;
                        addToSets(row, col, squareIndex, n);
                    }
                }

            }
        }
        return false;
    }

    private boolean wasNotChecked(int row, int col) {
        for (int i = 0; i < rowsColsChecked.size(); i++) {
            int[] rowCol = (int[]) rowsColsChecked.get(i);
            if (rowCol[0] == row && rowCol[1] == col) {
                return false;
            }
        }
        rowsColsChecked.add(new int[]{row, col});
        return true;
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

    private void removeFromSets(int row, int col, int squareIndex, int number) {
        rowSetList.get(row).remove(number);
        columnSetList.get(col).remove(number);
        squareSetList.get(squareIndex).remove(number);
    }

    private void addToSets(int row, int col, int squareIndex, int number) {
        rowSetList.get(row).add(number);
        columnSetList.get(col).add(number);
        squareSetList.get(squareIndex).add(number);
    }

    public List<Set> getRowSets() {
        return rowSetList;
    }

    public List<Set> getColSets() {
        return columnSetList;
    }

    public List<Set> getSquareSets() {
        return squareSetList;
    }

    private void initializeCopySets() {
        for (int i = 0; i < 9; i++) {
            columnSetListCopy.add(new HashSet<>(columnSetList));
            rowSetListCopy.add(new HashSet<>(rowSetList));
            squareSetListCopy.add(new HashSet<>(squareSetList));
        }
    }

    private void copySets() {
        for (int i = 0; i < 9; i++) {
            columnSetListCopy.get(i).clear();
            rowSetListCopy.get(i).clear();
            squareSetListCopy.get(i).clear();
            columnSetListCopy.get(i).addAll(columnSetList.get(i));
            rowSetListCopy.get(i).addAll(rowSetList.get(i));
            squareSetListCopy.get(i).addAll(squareSetList.get(i));
        }
    }

    private void copySetsBack() {
        for (int i = 0; i < 9; i++) {
            columnSetList.get(i).clear();
            rowSetList.get(i).clear();
            squareSetList.get(i).clear();
            columnSetList.get(i).addAll(columnSetListCopy.get(i));
            rowSetList.get(i).addAll(rowSetListCopy.get(i));
            squareSetList.get(i).addAll(squareSetListCopy.get(i));
        }
    }

    private void representBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                localBoard[i][j] = tilesArray[i][j].getNumber();
            }
        }
    }
}
