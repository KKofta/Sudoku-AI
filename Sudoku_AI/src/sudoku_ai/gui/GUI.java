package sudoku_ai.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {

    private static final double HEIGHT = 650;
    private static final double WIDTH = 1000;
    private static Board gameArea = new Board();

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 20, 10));

        createTitle(root, primaryStage);
        createMenu(root, primaryStage);
        createBoard(root);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("CSS/Styles.css").toExternalForm());

        primaryStage.setTitle("Sudoku with AI");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void createBoard(BorderPane root) {
        root.setCenter(gameArea);
    }

    private void createTitle(BorderPane root, Stage primaryStage) {

        VBox title = new VBox();
        title.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.1));

        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(0, 0, 15, 0));

        Text titleText = new Text("Sudoku with AI");
        titleText.setFont(Font.font("Verdana", 30));
        titleText.setFill(Color.WHITESMOKE);

        title.getChildren().addAll(titleText);
        root.setTop(title);
    }

    private void createMenu(BorderPane root, Stage primaryStage) {

        VBox leftMenu = new VBox(20);
        leftMenu.setAlignment(Pos.TOP_CENTER);
        leftMenu.setPadding(new Insets(0, 15, 0, 0));

        VBox rightMenu = new VBox(20);
        rightMenu.setAlignment(Pos.CENTER);

        //Solve it yourself area
        Label solveItYourselfLabel = new Label("Solve It Yourself");
        HBox solveItYourselfHBox = new HBox(10);
        solveItYourselfHBox.setAlignment(Pos.CENTER);

        Button solveItYourselfButton = new Button("Start Solving");
        CheckBox assistanceBox = new CheckBox("Assistance");
        solveItYourselfHBox.getChildren().addAll(solveItYourselfButton, assistanceBox);
        Button finishSolvingButton = new Button("Finish Attempt");

        //Solvers boards area
        Label solversLabel1 = new Label("Solve Boards");
        HBox solveVisHBox1 = new HBox(6);
        Button solveVisButton1 = new Button("Viusalize 1st Heuristic Algorithm");
        Button visInfoButton1 = new Button(" i ");
        visInfoButton1.setId("info1-button");
        solveVisHBox1.setAlignment(Pos.CENTER);
        solveVisHBox1.getChildren().addAll(solveVisButton1, visInfoButton1);
        HBox solveVisHBox2 = new HBox(6);
        Button solveVisButton2 = new Button("Viusalize Backtracking Algorithm");
        Button visInfoButton2 = new Button(" i ");
        visInfoButton2.setId("info2-button");
        solveVisHBox2.setAlignment(Pos.CENTER);
        solveVisHBox2.getChildren().addAll(solveVisButton2, visInfoButton2);
        
        //stop simulation area
        Button stopSimulationButton = new Button("Stop Simulation");
        stopSimulationButton.setId("stop-button");
        stopSimulationButton.setDisable(true);
        
        stopSimulationButton.setOnAction(e -> {
            gameArea.stopAnimationSolver();
            stopSimulationButton.setDisable(true);
            solveItYourselfButton.setDisable(false);
        });

        //action of solve buttons       
        solveItYourselfButton.setOnAction(e -> {
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
            finishSolvingButton.setDisable(false);
            gameArea.tilesActionOn();
        });

        assistanceBox.setSelected(true);
        gameArea.setAssistance(true);
        assistanceBox.setOnAction(e -> {
            if (assistanceBox.isSelected()) {
                gameArea.setAssistance(true);
            } else {
                gameArea.setAssistance(false);
                gameArea.turnOffRowsColsSquare();
            }
        });

        finishSolvingButton.setOnAction(e -> {
            gameArea.turnOffRowsColsSquare();
            if (gameArea.isBoardValid()) {
                displayMessage("Great! You solved this sudoku!");
            } else {
                displayMessage("Nope! This sudoku is not solved. Keep trying!");
            }
        });

        solveVisButton1.setOnAction(e -> {
            gameArea.animateEasySudoku();
            stopSimulationButton.setDisable(false);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
        });
        
        visInfoButton1.setOnAction(e -> {
            InformationMessage message = new InformationMessage("1st Heuristic");
            message.display();
        });

        solveVisButton2.setOnAction(e -> {
            gameArea.animateBacktrackingSudoku();
            stopSimulationButton.setDisable(false);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
        });
        
        visInfoButton2.setOnAction(e -> {
            InformationMessage message = new InformationMessage("Backtracking");
            message.display();
        });

        //Sample boards area
        Label samplesLabel = new Label("Load Sample Boards");
        HBox samplesHBox = new HBox(10);
        samplesHBox.setAlignment(Pos.CENTER);
        
        Button easyButton = new Button("Easy Board");
        easyButton.setId("easy-button");
        easyButton.setOnAction(e -> {
            gameArea.tilesActionOff();
            gameArea.loadEasyBoard();
            finishSolvingButton.setDisable(true);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, false);
        });
        
        Button intermediateButton = new Button("Intermediate Board");
        intermediateButton.setId("intermediate-button");
        intermediateButton.setOnAction(e -> {
            gameArea.tilesActionOff();
            gameArea.loadIntermediateBoard();
            finishSolvingButton.setDisable(true);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, false);
        });
        samplesHBox.getChildren().addAll(easyButton, intermediateButton);

        HBox samplesHBox2 = new HBox(10);
        samplesHBox2.setAlignment(Pos.CENTER);
        Button hardButton = new Button("Hard Board");
        hardButton.setId("hard-button");
        
        hardButton.setOnAction(e -> {
            gameArea.tilesActionOff();
            gameArea.loadHardBoard();
            finishSolvingButton.setDisable(true);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, false);
        });
        
        Button notFunButton = new Button("Not Fun Board");
        notFunButton.setId("notfun-button");
        notFunButton.setOnAction(e -> {
            gameArea.tilesActionOff();
            gameArea.loadNotFunBoard();
            finishSolvingButton.setDisable(true);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, false);
        });
        samplesHBox2.getChildren().addAll(hardButton, notFunButton);

        //Generate boards area
        Label generatorsLabel = new Label("Generate Boards");       
        
        HBox genHbox = new HBox(6);
        genHbox.setAlignment(Pos.CENTER);
        Button generateEasyButton = new Button("Visualize Easy Generation");
        generateEasyButton.setId("easygen-button");
        Button infoGenButton = new Button(" i ");
        infoGenButton.setId("infogen-button");
        genHbox.setAlignment(Pos.CENTER);
        genHbox.getChildren().addAll(generateEasyButton, infoGenButton);
        
        Button generateIntermediateButton = new Button("Visualize Intermediate Generation");
        generateIntermediateButton.setId("intermediategen-button");
        Button generateHardButton = new Button("Visualize Hard Generation");
        generateHardButton.setId("hardgen-button"); 
        
        HBox experHBox = new HBox(6);
        experHBox.setAlignment(Pos.CENTER);
        Button generateExperimentButton = new Button("Experimental Generation");
        generateExperimentButton.setId("exper-button");
        Button infoExpButton = new Button(" i ");
        infoExpButton.setId("infogen-button");
        experHBox.getChildren().addAll(generateExperimentButton, infoExpButton);
        
        HBox stopGenHBox = new HBox(10);
        stopGenHBox.setAlignment(Pos.CENTER);
        Button stopSimGenButton = new Button("Stop Simulation");
        stopSimGenButton.setId("stopgen-button");
        Button saveGenBoardButton = new Button("Save Board");
        stopGenHBox.getChildren().addAll(stopSimGenButton, saveGenBoardButton);
        
        infoGenButton.setOnAction(e -> {
            InformationMessage message = new InformationMessage("Generator");
            message.display();
        });
        
        generateEasyButton.setOnAction(e -> {
            gameArea.animateGenerator(46, false);
            stopSimGenButton.setDisable(false);
            saveGenBoardButton.setDisable(false);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
        });
        
        generateIntermediateButton.setOnAction(e -> {
            gameArea.animateGenerator(51, false);
            stopSimGenButton.setDisable(false);
            saveGenBoardButton.setDisable(false);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
        });
        
        generateHardButton.setOnAction(e -> {
            gameArea.animateGenerator(61, false);
            stopSimGenButton.setDisable(false);
            saveGenBoardButton.setDisable(false);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
        });
        
        generateExperimentButton.setOnAction(e ->{
            gameArea.animateGenerator(64, true);
            stopSimGenButton.setDisable(false);
            saveGenBoardButton.setDisable(false);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
        });
        
        infoExpButton.setOnAction(e -> {
            InformationMessage message = new InformationMessage("Experiment");
            message.display();
        });
        
        stopSimGenButton.setOnAction(e -> {
            gameArea.stopAnimationGenerator();
            stopSimGenButton.setDisable(true);
            generateEasyButton.setDisable(false);
        });
        
        saveGenBoardButton.setOnAction(e -> {
            gameArea.saveGeneratedBoard();
            saveGenBoardButton.setDisable(true);
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, false);
        });

        Label myBoardLabel = new Label("Create Your Own Board");
        HBox myBoardHBox = new HBox(10);
        myBoardHBox.setAlignment(Pos.CENTER);
        Button createBoardButton = new Button("Create Board");
        Button saveBoardButton = new Button("Save Board");
        myBoardHBox.getChildren().addAll(createBoardButton, saveBoardButton);
        
        createBoardButton.setOnAction(e -> {
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
            saveBoardButton.setDisable(false);
            gameArea.tilesActionOn();
        });
        
        saveBoardButton.setOnAction(e -> {
            saveBoardButton.setDisable(true);
            gameArea.tilesActionOff();
            gameArea.turnOffRowsColsSquare();
            gameArea.saveBoard();
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, false);
        });

        //clear board area
        Button clearButton = new Button("Clear Board");
        clearButton.setOnAction(e -> {
            gameArea.tilesActionOff();
            gameArea.clearBoard();
            disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
            finishSolvingButton.setDisable(true);
            saveBoardButton.setDisable(true);
        });

        //exit area
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        HBox bottomMenu = new HBox(30);
        bottomMenu.setAlignment(Pos.CENTER);
        bottomMenu.getChildren().addAll(clearButton, exitButton);
        root.setBottom(bottomMenu);
        
        disableSolveButtons2(solveItYourselfButton, solveVisButton1, solveVisButton2, true);
        finishSolvingButton.setDisable(true);
        saveBoardButton.setDisable(true);
        stopSimGenButton.setDisable(true);
        saveGenBoardButton.setDisable(true);

        leftMenu.getChildren().addAll(samplesLabel, samplesHBox, samplesHBox2,
                myBoardLabel, myBoardHBox, generatorsLabel,genHbox, generateIntermediateButton, 
                generateHardButton, experHBox,stopGenHBox);
        root.setLeft(leftMenu);

        rightMenu.getChildren().addAll(solveItYourselfLabel, solveItYourselfHBox,
                finishSolvingButton, solversLabel1, solveVisHBox1, solveVisHBox2, stopSimulationButton);
        root.setRight(rightMenu);
    }

    private void disableSolveButtons2(Button button1, Button button2, Button button3, boolean disable) {
        button1.setDisable(disable);
        button2.setDisable(disable);
        button3.setDisable(disable);
    }

    public void displayMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Board Validation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("CSS/Styles.css").toExternalForm());
        alert.showAndWait();
    }
    
    public static Board getBoard() {
        return gameArea;
    }

}
