package sudoku_ai.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Tile extends StackPane {

    private Rectangle border = new Rectangle();
    private Text text = new Text();
    //private final int IDx;
    //private final int IDy;

    public Tile(double tileSize/*, int IDx, int IDy*/) {
        //this.IDx = IDx;
        //this.IDy = IDy;
        this.border.setWidth(tileSize);
        this.border.setHeight(tileSize);
        this.border.setFill(null);
        this.border.setStroke(Color.WHITESMOKE);
        this.border.setStrokeWidth(1.5);

        text.setFont(Font.font(30));
        text.setFill(Color.WHITESMOKE);
        text.setStroke(Color.WHITESMOKE);
        text.setTextAlignment(TextAlignment.CENTER);
        setAlignment(this.border, Pos.CENTER);
        setAlignment(this.text, Pos.CENTER);
        setMaxSize(tileSize, tileSize);

        getChildren().addAll(this.border, this.text);
    }

    public void setNumber(int number/*, int IDx, int IDy*/) {
        String numberString;
        if (number == 0) {
            numberString = "";
        } else {
            numberString = Integer.toString(number);
        }
        this.text.setText(numberString);
        text.setFill(Color.WHITESMOKE);
        text.setStroke(Color.WHITESMOKE);
    }
    
    public void setCalculatedNumber(int number) {
        //numbers calculated with algorithm in different color 
        String numberString;
        if (number == 0) {
            numberString = "";
        } else {
            numberString = Integer.toString(number);
        }
        this.text.setText(numberString);
        text.setFill(Color.YELLOW);
        text.setStroke(Color.YELLOW);
    }

    public int getNumber() {
        String numberString = this.text.getText();
        if (numberString.equals("")) {
            return 0;
        }
        return Integer.parseInt(numberString);
    }

    /*public int getIDx() {
        return this.IDx;
    }

    public int getIDy() {
        return this.IDy;
    }*/
    
    public void setGreenBorderColor() {
        this.border.setStroke(Color.GREEN);
        this.border.setStrokeWidth(4);
        this.border.setStyle("-fx-stroke: green; -fx-stroke-width: 4");
    }
    
    public void setWhiteBorderColor() {
        this.border.setStroke(Color.WHITESMOKE);
        this.border.setStrokeWidth(4);
        this.border.setStyle("-fx-stroke: whitesmoke; -fx-stroke-width: 1.5");
    }
    
}
