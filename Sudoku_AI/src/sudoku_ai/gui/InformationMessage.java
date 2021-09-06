package sudoku_ai.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class InformationMessage {

    private String infoMessage = new String();
    private String header = new String();

    public InformationMessage(String messageType) {
        assignMessage(messageType);
    }

    public void display() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Algorithm Information");
        alert.setHeaderText(header);
        alert.setContentText(infoMessage);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("CSS/Styles.css").toExternalForm());
        dialogPane.setMinSize(600, 250);
        alert.showAndWait();
    }

    private void assignMessage(String messageType) {
        if (messageType.equals("1st Heuristic")) {
            header = "1st Heuristic Algorithm";
            infoMessage = HEURISTIC_1ST;
        } else if (messageType.equals("Backtracking")) {
            header = "Backtracking Algorithm";
            infoMessage = BACKTRACKING;
        } else if (messageType.equals("Generator")) {
            header = "Basic Generator";
            infoMessage = GENERATOR;
        } else if (messageType.equals("Experiment")) {
            header = "Generator using Backtracking";
            infoMessage = EXPERIMENT;
        }
    }

    private String HEURISTIC_1ST = "It is a very basic algorithm "
            + "which allows to solve easy sudoku boards. If a board require more "
            + "advanced techniques the algorithm will not work.\n\n"
            + "It iterates through every cell on a board:\n"
            + "1. Checks if it is possible to put in no more than one number:\n"
            + "\ta. If it is it puts it in and checks next cell.\n"
            + "\tb. Otherwise it leaves cell empty and checks next cell.\n"
            + "2. When it comes to the last cell:\n"
            + "\ta. If the board is fully filled it ends algorithm.\n"
            + "\tb. Otherwise it comes back to the 1st cell.\n\n";

    private String BACKTRACKING = "The backtracking algorithm is a type of recursive brute "
            + "force search (depth-first search - it will completely explore one "
            + "branch to a possible solution before moving to another branch). \n"
            + "Cells are represented as the nodes of a tree structure. Each cell is the parent of the cells"
            + " that differ from it by a single extension step.\n"
            + "The backtracking algorithm traverses this search tree recursively, from the root down, in depth-first order. \n\nExample:\n"
            + "Algorithm visits the empty cells in some order, filling in digits sequentially, "
            + "or backtracking when the number is found to be not valid. Briefly, "
            + "a program would solve a puzzle by placing the digit \"1\" in the first cell and checking if it is allowed to be there. "
            + "If there are no violations (checking row, column, and box constraints) then the algorithm advances to the next cell and "
            + "places a \"1\" in that cell. When checking for violations, if it is discovered that the \"1\" is not allowed, the value "
            + "is advanced to \"2\". If a cell is discovered where none of the 9 digits is allowed, then the algorithm leaves that cell "
            + "blank and moves back to the previous cell. The value in that cell is then incremented by one. This is repeated until the"
            + " allowed value in the last (81st) cell is discovered. \n"
            + "\t • A solution is guaranteed if it exists. If a board has more than 1 solution we could find them all with backtracking. However it was not necessary here.\n"
            + "\t • Solving time is mostly unrelated to degree of difficulty.";

    private String GENERATOR = "To generate a valid sudoku board (board with only 1 possible solution) we follow these 3 steps:\n"
            + "1. Fill diagonal boxes with randomly generated digits from 1 to 9. Diagonal boxes are not in relation with each other "
            + "so we don't need to check whether a digit already exist in a certian row or column. We just check existance in a certian box.\n"
            + "2. Fill the rest of empty cells in non-diagonal boxes with digits from 1 to 9 while checking if each is not already used in box, row or column. "
            + "To achieve that we use Backtracking Algorithm which will give us a fully filled valid Sudoku board.\n"
            + "3. From a generated valid Sudoku board we remove random cells (one by one). We check if the board after removal of a cell remain "
            + "solvable using 1st Heuristic Algorithm (since in this algorithm we always fill the cell with a digit that is "
            + "the only possible digit, the resulting board has to be unique - valid):\n"
            + "\t a. If after the removal of a cell the board is still solvable (has 1 possible solution) it remains removed and the proceed to check another cell.\n"
            + "\t b. Otherwise we put the number back and try to remove number from another cell.\n"
            + "We repeat removing until the desired number of digits remain on a grid or until there is no cell left to be removed which would keep board solvable.\n\n"
            + "\t • The generated board has unique solution and since we used 1st Heuristic Algorithm to check solvability it should be relatively easily solvable for human.\n"
            + "\t • The same generator algorithm was used for Intermediate and Hard versions. They differ in a number of digits removed from a board.";

    private String EXPERIMENT = "To generate a valid sudoku board (board with only 1 possible solution) we follow these 3 steps:\n"
            + "1. Fill diagonal boxes with randomly generated digits from 1 to 9. Diagonal boxes are not in relation with each other "
            + "so we don't need to check whether a digit already exist in a certian row or column. We just check existance in a certian box.\n"
            + "2. Fill the rest of empty cells in non-diagonal boxes with digits from 1 to 9 while checking if each is not already used in box, row or column. "
            + "To achieve that we use Backtracking Algorithm which will give us a fully filled valid Sudoku board.\n"
            + "3. From a generated valid Sudoku board we remove random cells (one by one). We check if the board after removal of a cell remain "
            + "solvable using Backtracking Algorithm (the algorithm is slightly modified compared to a solver, because it checks whether the board has more than 1 solution.):\n"
            + "\t a. If after the removal of a cell the board is still solvable (has 1 possible solution) it remains removed and the proceed to check another cell.\n"
            + "\t b. Otherwise we put the number back and try to remove number from another cell.\n"
            + "We repeat removing until the desired number of digits remain on a grid or until there is no cell left to be removed which would keep board solvable.\n\n"
            + "\t • The generated board has unique solution, but since we used Backtracking Algorithm to check solvability it is likely that board is very hard to solve by human techniques.\n"
            + "\t • This approach can be used for more experimental purposes - attempts to generate board with the fewest clues. (my record is 22 :P).";
}
