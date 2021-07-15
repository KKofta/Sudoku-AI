package sudoku_ai.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {

    private static final double HEIGHT = 600;
    private static final double WIDTH = 750;
    private static final double BOARD_SIZE = HEIGHT - HEIGHT * 0.2;

    Board gameArea = new Board(BOARD_SIZE);

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

        VBox menu = new VBox(30);
        menu.setAlignment(Pos.CENTER);
        menu.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.));
        menu.setPadding(new Insets(10, 20, 15, 0));

        Button sampleBoardButton = new Button("Load Sample Board");
        sampleBoardButton.setOnAction(e -> gameArea.loadSampleBoard());

        Button clearButton = new Button("Clear Board");
        clearButton.setOnAction(e -> gameArea.clearBoard());

        Button generateButton = new Button("Generate Board");
        //action

        Button generateVisButton = new Button("Generate Board & Visualize");
        //action

        Button solveButton = new Button("Use Algorithm");
        solveButton.setOnAction(e -> gameArea.solveSudoku());

        Button solveVisButton = new Button("Use Algorithm & Viusalize");
        //action

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        menu.getChildren().addAll(sampleBoardButton, clearButton, generateButton,
                generateVisButton, solveButton, solveVisButton, exitButton);

        root.setRight(menu);
    }

}
