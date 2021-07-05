package sudoku_ai.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GUI extends Application {

    private static final double HEIGHT = 600;
    private static final double WIDTH = 600;
    private static final double BOARD_SIZE = HEIGHT - HEIGHT * 0.1;

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();
        createTitle(root, primaryStage);
        
        Board gameArea = new Board(BOARD_SIZE);
        root.setCenter(gameArea);

        Scene scene = new Scene(root, HEIGHT, WIDTH);

        primaryStage.setTitle("Sudoku with AI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createTitle(BorderPane root, Stage primaryStage) {
        
        VBox title = new VBox();
        //multiply to set size (0.80 is like 10% of the window) - do ew późniejszego poprawienia boardu i tile
        title.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.1));
        
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(10, 0, 0, 0));

        Text titleText = new Text("Sudoku with AI");
        titleText.setFont(Font.font("Verdana", 30));
        titleText.setFill(Color.BLACK);

        title.getChildren().addAll(titleText);
        root.setTop(title);
    }

}
