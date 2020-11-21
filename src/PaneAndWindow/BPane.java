package PaneAndWindow;

import bTreeCode.BTreeCode2;
import javafx.animation.FillTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BPane extends Pane {

    public BTreeCode2 bTree = new BTreeCode2(3);//initialising a tree with m=3
    private double originalX, originalY;

    // TODO: make node size relate to pane's size
    private final int fontSize = 14;
    private final int rectangleWidth = 30;
    private final int rowSpace = 60;

    public BPane(double x, double y, BTreeCode2 bTree) {
        this.originalX = x;
        this.originalY = y;
        this.bTree = bTree;
    }


    private void DrawNode(String s, double x, double y, Color color) {
        Rectangle rect = new Rectangle(x, y, rectangleWidth, rectangleWidth);
        rect.setFill(color);
        rect.setStroke(Color.WHITESMOKE);
        rect.setArcHeight(10); rect.setArcWidth(10);
        Text txt = new Text(x + 11 - s.length(), y + 20, s);
        txt.setFill(Color.WHITE);
        txt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, fontSize));
        this.getChildren().addAll(rect, txt);
    }



}
