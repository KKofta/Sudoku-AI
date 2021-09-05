package sudoku_ai.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class InformationMessage {
    
    private String infoMessage = new String();

    public InformationMessage(String messageType) {
        assignMessage(messageType);
    }

    public void display() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Algorithm Information");
        alert.setHeaderText(null);
        alert.setContentText(infoMessage);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("CSS/Styles.css").toExternalForm());
        alert.showAndWait();
    }
    
    private void assignMessage(String messageType){
        if (messageType.equals("1st Heuristic")) {
            infoMessage = HEURISTIC_1ST;
        } else if (messageType.equals("Backtracking")) {
            infoMessage = BACKTRACKING;
        } else if (messageType.equals("Generator")) {
            infoMessage = GENERATOR;
        } else if (messageType.equals("Experiment")){
            infoMessage = EXPERIMENT;
        }
    }
    
    private String HEURISTIC_1ST = "HEURISTIC_1ST";
    
    private String BACKTRACKING = "BACKTRACKING";
    
    private String GENERATOR = "GENERATOR";
    
    private String EXPERIMENT = "EXPERIMENT";

}
