package sudoku_ai.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {

    private static final double HEIGHT = 600;
    private static final double WIDTH = 800;
    private Board gameArea = new Board();

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15, 15, 15, 15));

        createTitle(root, primaryStage);
        createMenu(root, primaryStage);
        createBoard(root);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("CSS/Styles.css").toExternalForm());

        primaryStage.setTitle("Sudoku with AI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createBoard(BorderPane root) {
        root.setCenter(gameArea);
    }

    private void createTitle(BorderPane root, Stage primaryStage) {

        VBox title = new VBox();
        //multiply to set size (0.80 is like 10% of the window) - do ew późniejszego poprawienia boardu i tile
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

        VBox menu = new VBox(20);
        menu.setAlignment(Pos.CENTER);
        menu.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.));

        //stop simulation
        Button stopSimulationButton = new Button("Stop Simulation");
        stopSimulationButton.setDisable(true);
        stopSimulationButton.setOnAction(e -> {
            gameArea.stopBacktrackingAnimation();
            stopSimulationButton.setDisable(true);
        });

        //Solvers boards area
        Label solversLabel1 = new Label("Solve Boards");
        HBox solversHBox1 = new HBox(10);
        Button solveButton1 = new Button("1st Heuristic Algorithm");
        solveButton1.setOnAction(e -> gameArea.solveEasySudoku());
        Button solveVisButton1 = new Button("Viusalize");
        solveVisButton1.setOnAction(e -> {
            gameArea.animateEasySudoku();
            stopSimulationButton.setDisable(false);
        });

        solversHBox1.getChildren().addAll(solveButton1, solveVisButton1);
        solversHBox1.setAlignment(Pos.CENTER);

        HBox solversHBox2 = new HBox(10);
        Button solveButton2 = new Button("Backtracking Algorithm");
        solveButton2.setOnAction(e -> gameArea.solveBacktrackingSudoku());
        Button solveVisButton2 = new Button("Viusalize");
        solveVisButton2.setOnAction(e -> {
            gameArea.animateBacktrackingSudoku();
            stopSimulationButton.setDisable(false);
        });

        disableSolveButtons(solveButton1, solveVisButton1, solveButton2, solveVisButton2, true);
        solversHBox2.getChildren().addAll(solveButton2, solveVisButton2);
        solversHBox2.setAlignment(Pos.CENTER);
        //solversHBox2.setPadding(new Insets(0, 0, 25, 0));

        //Sample boards area
        Label samplesLabel = new Label("Load Sample Boards");
        HBox samplesHBox = new HBox(10);
        Button easyButton = new Button("Easy Board");
        easyButton.setOnAction(e -> {
            gameArea.loadEasyBoard();
            disableSolveButtons(solveButton1, solveVisButton1, solveButton2, solveVisButton2, false);
        });
        Button intermediateButton = new Button("Intermediate Board");
        intermediateButton.setOnAction(e -> {
            gameArea.loadIntermediateBoard();
            disableSolveButtons(solveButton1, solveVisButton1, solveButton2, solveVisButton2, false);
        });
        samplesHBox.setAlignment(Pos.CENTER);
        samplesHBox.getChildren().addAll(easyButton, intermediateButton);
        
        HBox samplesHBox2 = new HBox(10);
        Button hardButton = new Button("Hard Board");
        hardButton.setOnAction(e -> {
            gameArea.loadHardBoard();
            disableSolveButtons(solveButton1, solveVisButton1, solveButton2, solveVisButton2, false);
        });
        Button notFunButton = new Button("Not Fun Board");
        notFunButton.setOnAction(e -> {
            gameArea.loadNotFunBoard();
            disableSolveButtons(solveButton1, solveVisButton1, solveButton2, solveVisButton2, false);
        });
        samplesHBox2.setAlignment(Pos.CENTER);
        samplesHBox2.getChildren().addAll(hardButton, notFunButton);

        //Generate boards area
        Label generatorsLabel = new Label("Generate Boards");
        HBox generatorsHBox = new HBox(10);
        Button generateButton = new Button("Generate");
        //action
        Button generateVisButton = new Button("Visualize");
        //action
        generatorsHBox.setAlignment(Pos.CENTER);
        generatorsHBox.getChildren().addAll(generateButton, generateVisButton);

        //clear board area
        Button clearButton = new Button("Clear Board");
        clearButton.setOnAction(e -> {
            gameArea.clearBoard();
            disableSolveButtons(solveButton1, solveVisButton1, solveButton2, solveVisButton2, true);
        });

        //exit area
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        menu.getChildren().addAll(samplesLabel, samplesHBox, samplesHBox2, generatorsLabel, generatorsHBox,
                solversLabel1, solversHBox1, solversHBox2, stopSimulationButton, clearButton, exitButton);

        root.setRight(menu);
    }

    private void disableSolveButtons(Button button1, Button button2, Button button3, Button button4, boolean disable) {
        button1.setDisable(disable);
        button2.setDisable(disable);
        button3.setDisable(disable);
        button4.setDisable(disable);
    }

}
