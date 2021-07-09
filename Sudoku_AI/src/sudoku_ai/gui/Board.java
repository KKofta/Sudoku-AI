package sudoku_ai.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

        for (int i = 0; i < 9; i++) { //I messed up indexes, because on GUI I was thinking as x,y - reversely to arrays
            for (int j = 0; j < 9; j++) {
                int number = sampleArray[i][j]; //change [i][j] to [j][i] for now
                tilesArray[i][j].setNumber(number/*, i, j*/);
            }
        }
    }

    public void generateBoard() {
        int size = 9;
        Random rand = new Random();
        List<Integer> list = new ArrayList<>(size);

        for (int rowNr = 0; rowNr < 4; rowNr++) {
            //adding elements 1-9 to list
            for (int i = 1; i <= size; i++) {
                list.add(i);
            }

            int columnNr = 0;
            while (list.size() > 0) {
                int index = rand.nextInt(list.size());
                int number = list.get(index);

                //System.out.println(Arrays.toString(list.toArray()));
                //System.out.println("#" + columnNr + " Index: " + index + " | Number: " + number);
                boolean checkCol = isInColumn(rowNr, columnNr, number);

                if (checkCol == false) {
                    tilesArray[rowNr][columnNr].setNumber(number/*, columnNr, rowNr*/);
                    list.remove(index);
                    columnNr++;
                }
            }
            //System.out.println("------------------------------------------------------------------------");
        }
    }

    private boolean isInColumn(int rowNr, int columnNr, int numberToCompare) {
        for (int row = 0; row < rowNr; row++) {
            //System.out.println("isInColumn: columnNr: "+columnNr+" | rowNr: "+rowNr+" | NrToCompare: "+numberToCompare);
            System.out.println("Number from row #" + row + ": " + tilesArray[row][columnNr].getNumber());
            System.out.println("Random number: " + numberToCompare);
            if (tilesArray[row][columnNr].getNumber() == numberToCompare && columnNr == 8) {
                //System.out.println("ZAPĘTLONA!!!???    |     Row NR: " + rowNr);
            } else if (tilesArray[row][columnNr].getNumber() == numberToCompare) {
                //System.out.println("JEST??        |     Row NR: " + rowNr);
                return true;
            }
        }
        return false;
    }

    public void setNumber(int number, int IDx, int IDy) {
        tilesArray[IDx][IDy].setNumber(number/*, IDx, IDy*/);
    }

    /*public void generateBoardv2(){
        int range = 9;
        Set<Integer> middle = new LinkedHashSet<>();
        Random rand = new Random();
        
        while(middle.size() < range){
            Integer next = rand.nextInt(range) + 1;
            middle.add(next);
            System.out.println(next);
            System.out.println(Arrays.toString(middle.toArray()));
        }  
    }
     */
}
