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
    //private static final double BOARD_SIZE = HEIGHT - HEIGHT * 0.2;
    private static final double BOARD_SIZE = 500;

    private Board gameArea = new Board(BOARD_SIZE);

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15,15,15,15));

        createTitle(root, primaryStage);
        createMenu(root, primaryStage);
        createBoard(root);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());

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
        menu.setPadding(new Insets(10, 20, 15, 0));

        Label samplesLabel = new Label("Load Sample Boards");
        HBox samplesHBox = new HBox(10);
        Button easyButton = new Button("Easy Board");
        easyButton.setOnAction(e -> gameArea.loadEasyBoard());
        Button hardButton = new Button("Hard Board");
        hardButton.setOnAction(e -> gameArea.loadHardBoard());
        samplesHBox.getChildren().addAll(easyButton, hardButton);

        Label generatorsLabel = new Label("Generate Boards");
        HBox generatorsHBox = new HBox(10);
        Button generateButton = new Button("Generate");
        //action
        Button generateVisButton = new Button("Visualize");
        //action
        generatorsHBox.getChildren().addAll(generateButton, generateVisButton);

        Label solversLabel = new Label("Solve Boards");
        HBox solversHBox = new HBox(10);
        Button solveButton = new Button("First Algorithm");
        solveButton.setOnAction(e -> gameArea.solveSudoku());
        Button solveVisButton = new Button("Viusalize");
        //action
        solversHBox.getChildren().addAll(solveButton, solveVisButton);
        solversHBox.setPadding(new Insets(0,0,25,0));
        
        Button clearButton = new Button("Clear Board");
        clearButton.setOnAction(e -> gameArea.clearBoard());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        menu.getChildren().addAll(samplesLabel, samplesHBox, generatorsLabel, generatorsHBox,
                solversLabel, solversHBox, clearButton, exitButton);

        root.setRight(menu);
    }

}
