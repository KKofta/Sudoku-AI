package sudoku_ai.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Tile extends StackPane {    
    
    Rectangle border = new Rectangle();
    Text text = new Text("1");

    public Tile(double tileSize) {
        this.border.setWidth(tileSize);
        this.border.setHeight(tileSize);
        this.border.setFill(null);
        this.border.setStroke(Color.BLACK);
        
        text.setTextAlignment(TextAlignment.CENTER);
        setAlignment(this.border, Pos.CENTER);
        setAlignment(this.text, Pos.CENTER);
        setMaxSize(tileSize, tileSize);
        
        getChildren().addAll(this.border, this.text);
    }
    
    public void setGreenBorderColor(){
        this.border.setStroke(Color.GREEN);
    }

}
