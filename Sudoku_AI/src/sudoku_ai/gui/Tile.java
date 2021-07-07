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
    private final int IDx;
    private final int IDy;

    public Tile(double tileSize, int IDx, int IDy) {
        this.IDx = IDx;
        this.IDy = IDy;
        this.border.setWidth(tileSize);
        this.border.setHeight(tileSize);
        this.border.setFill(null);
        this.border.setStroke(Color.BLACK);
        this.border.setStrokeWidth(1.5);

        text.setFont(Font.font(30));
        text.setTextAlignment(TextAlignment.CENTER);
        setAlignment(this.border, Pos.CENTER);
        setAlignment(this.text, Pos.CENTER);
        setMaxSize(tileSize, tileSize);

        getChildren().addAll(this.border, this.text);
    }

    public void setGreenBorderColor() {
        this.border.setStroke(Color.GREEN);
    }
    
    public void setNumber(int number, int IDx, int IDy){
        String numberString;
        if(number == 0){
            numberString = "";
        } else {
            numberString = Integer.toString(number);
        }
        this.text.setText(numberString);
    }
    
    public int getIDx(){
        return this.IDx;
    }
    
    public int getIDy(){
        return this.IDy;
    }

}
