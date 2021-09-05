package sudoku_ai.gui;

import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Tile extends StackPane {

    private Rectangle border = new Rectangle();
    private Text text = new Text();
    private int row;
    private int col;

    public Tile(double tileSize) {
        border.setWidth(tileSize);
        border.setHeight(tileSize);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.WHITESMOKE);
        border.setStrokeWidth(1.5);

        text.setFont(Font.font(30));
        text.setFill(Color.WHITESMOKE);
        text.setStroke(Color.WHITESMOKE);
        text.setTextAlignment(TextAlignment.CENTER);
        setAlignment(border, Pos.CENTER);
        setAlignment(text, Pos.CENTER);
        setMaxSize(tileSize, tileSize);

        getChildren().addAll(border, text);
    }

    public void setNumber(int number) {
        String numberString;
        if (number == 0) {
            numberString = "";
        } else {
            numberString = Integer.toString(number);
        }
        text.setText(numberString);
        text.setFill(Color.WHITESMOKE);
        text.setStroke(Color.WHITESMOKE);
        //Color.
    }

    public void setCalculatedNumber(int number) {
        //numbers calculated with algorithm are in different color 
        String numberString;
        if (number == 0) {
            numberString = "";
        } else {
            numberString = Integer.toString(number);
        }
        text.setText(numberString);
        text.setFill(Color.GOLD);
        text.setStroke(Color.GOLD);
    }

    public void changeYellowToWhite() {
        if (!text.getText().isEmpty() || text.getFill().equals(Color.GOLD)) {
            text.setFill(Color.WHITESMOKE);
            text.setStroke(Color.WHITESMOKE);
        }
    }

    public int getNumber() {
        String numberString = text.getText();
        if (numberString.equals("")) {
            return 0;
        }
        return Integer.parseInt(numberString);
    }

    public void setGreenBorderColor() {
        border.setStroke(Color.GREEN);
        border.setStrokeWidth(4);
        border.setStyle("-fx-stroke: green; -fx-stroke-width: 4");
    }

    public void setWhiteBorderColor() {
        border.setStroke(Color.WHITESMOKE);
        border.setStrokeWidth(1.5);
        border.setStyle("-fx-stroke: whitesmoke; -fx-stroke-width: 1.5");
    }

    public void setTileActionOn() {
        border.setOnMouseEntered(enter -> {
            setGreenBorderColor();
        });

        border.setOnMouseExited((event) -> {
            setWhiteBorderColor();
        });

        border.setOnMouseClicked(event -> {

            Board gameArea = GUI.getBoard();

            if (!text.isFocused()) {
                gameArea.lightRowsColsSquare(this);

                text.requestFocus();
                if (text.getText().isEmpty() || text.getFill().equals(Color.GOLD)) {
                    this.text.setOnKeyPressed((e) -> {
                        if (e.getCode().equals(KeyCode.DIGIT1)) {
                            setCalculatedNumber(1);
                            gameArea.removeFromSets(row, col, 1);
                        } else if (e.getCode().equals(KeyCode.DIGIT2)) {
                            setCalculatedNumber(2);
                            gameArea.removeFromSets(row, col, 2);
                        } else if (e.getCode().equals(KeyCode.DIGIT3)) {
                            setCalculatedNumber(3);
                            gameArea.removeFromSets(row, col, 3);
                        } else if (e.getCode().equals(KeyCode.DIGIT4)) {
                            setCalculatedNumber(4);
                            gameArea.removeFromSets(row, col, 4);
                        } else if (e.getCode().equals(KeyCode.DIGIT5)) {
                            setCalculatedNumber(5);
                            gameArea.removeFromSets(row, col, 5);
                        } else if (e.getCode().equals(KeyCode.DIGIT6)) {
                            setCalculatedNumber(6);
                            gameArea.removeFromSets(row, col, 6);
                        } else if (e.getCode().equals(KeyCode.DIGIT7)) {
                            setCalculatedNumber(7);
                            gameArea.removeFromSets(row, col, 7);
                        } else if (e.getCode().equals(KeyCode.DIGIT8)) {
                            setCalculatedNumber(8);
                            gameArea.removeFromSets(row, col, 8);
                        } else if (e.getCode().equals(KeyCode.DIGIT9)) {
                            setCalculatedNumber(9);
                            gameArea.removeFromSets(row, col, 9);
                        } else if (e.getCode().equals(KeyCode.DIGIT0)
                                || e.getCode().equals(KeyCode.BACK_SPACE) || e.getCode().equals(KeyCode.DELETE)) {
                            int number = Integer.parseInt(text.getText());
                            gameArea.addToSets(row, col, number);
                            setCalculatedNumber(0);
                        }
                    });
                }
            } else {
                gameArea.turnOffRowsColsSquare();
                this.border.requestFocus();
            }
        });
    }

    public void setTileActionOff() {
        border.setOnMouseEntered(enter -> {
        });

        border.setOnMouseExited((event) -> {
        });

        border.setOnMouseClicked(event -> {
        });
    }

    public void lightMainTile() {
        border.setFill(Color.LIGHTSLATEGRAY);
    }

    public void lightTile() {
        border.setFill(Color.GRAY);
    }

    public void turnOffTile() {
        border.setFill(Color.TRANSPARENT);
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

}
