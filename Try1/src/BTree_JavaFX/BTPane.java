package BTree_JavaFX;

import javafx.animation.FillTransition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BTPane extends Pane {
    BTree_JavaFX.BTree<Integer> bTree; // object of class Btree
    double X, Y;   // coordinates

    public BTPane(double x, double y, BTree<Integer> bTree) {
        this.X = x+200;
        this.Y = y+200;
        this.bTree = bTree;
    }

    public void MakeNode(String s, double x, double y) {
        Text nn = new Text(x + 18 - s.length(), y + 30, s); // Text of key value in the node
        nn.setFill(Color. rgb(20, 1, 0)  );
        nn.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 14));

        Rectangle nodeBox = new Rectangle(x, y, 50, 50); // node box configuration
        nodeBox.setFill(Color.rgb(140, 211, 255));
        nodeBox.setStroke(Color.rgb(35, 39, 42)  );
        nodeBox.setArcHeight(15); nodeBox.setArcWidth(20);

        this.getChildren().addAll(nodeBox, nn);
    }

    public void paneUpdater(BTree<Integer> bTree) {
        this.getChildren().clear(); // Clear screen & reprint
        this.bTree = bTree;
        makeBTree(bTree.getRoot(), X, Y); // Call to makeBTree function
    }


    public void FindNode(BTree<Integer> bTree, int key) throws Exception {
        paneUpdater(bTree);
        if (!bTree.isEmpty()) { //Check for empty tree
            BTree_JavaFX.BTNode<Integer> current = bTree.getRoot();
            double x = X, y = Y;
            double pause = 0;
            while (!current.equals(bTree.nullBTNode)) {
                int i = 0;
                while (i < current.getSize()) {
                    colorFoundNode(current.getKey(i).toString(), x, y, pause);
                    pause+= 0.5;
                    if (current.getKey(i).equals(key)) { // Compare the key
                        return;
                    } else if (current.getKey(i).compareTo(key) > 0) { // key < searched value
                        y += 60; //Coming Down
                        if ((double) i < ((double) current.getSize()) / 2) {
                            x = x - (bTree.getOrder() - 1) * (bTree.getHeight(current.getTheChildAtIndex(i))-1) * 50.0 / 2 - ((double) current.getTheChildAtIndex(i).getSize()) * 50; //coordinate updated
                        } else {
                            x = x - ((double) current.getTheChildAtIndex(i).getSize()) / 2 * 50;
                        }
                        if (i == 0) {
                            x -= 50 * 2;
                        }

                        current = current.getTheChildAtIndex(i);
                        i = 0;
                    } else {
                        i++;  //next child
                        x += 50;
                    }
                }

                if (!current.isNull()) {
                    y += 60;
                    x = x + (bTree.getOrder() - 1) * (bTree.getHeight(current.getTheChildAtIndex(i))-1) * 50.0 / 2 + 50 * 2; // coordinate updated

                    current = current.getTheChildAtIndex(current.getSize());
                }
            }
        }
        throw new Exception("Number is not in the tree!");
    }

    private void colorFoundNode(String s, double x, double y, double delay) {

        Text nn = new Text(x + 18 - s.length(), y + 30, s);
        nn.setFill(Color.WHITE);
        nn.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));

        Rectangle rect = new Rectangle(x, y, 50, 50);  // Draw a node
        rect.setFill(Color.rgb(140, 211, 255));
        rect.setStroke(Color.rgb( 245, 245, 245));
        rect.setArcHeight(15); rect.setArcWidth(20);

        this.getChildren().addAll(rect, nn);

        // make fill transition ...this makes node change its colors when it is traversed on
        FillTransition fill = new FillTransition();

        fill.setAutoReverse(false);
        fill.setCycleCount(1);
        fill.setDelay(Duration.seconds(delay));
        fill.setDuration(Duration.seconds(.5));
        fill.setFromValue(Color.rgb(140, 211, 255));
        fill.setToValue(Color.rgb(255, 206, 10));
        fill.setShape(rect);
        fill.play();
    }

    private void makeBTree(BTree_JavaFX.BTNode<Integer> root, double x, double y) {
        if (root != null) {
            // Making keys of node
            for (int i = 0; i < root.getSize(); i++) {
                MakeNode(root.getKey(i).toString(), x + i * 50, y );
            }
            // Draw line
            double lineY = y + 2 * 14;
            if (!root.trueIfLastInternalNode()) {
                for (int i = 0; i < root.getChildren().size(); i++) {
                    double eol = x + i * 50; //end of Line
                    double soc = 0, eox = 0;   // start of child nodes

                    if ((double) i > ((double) root.getSize()) / 2) {
                        soc = eol
                                + (bTree.getOrder() - 1) * (bTree.getHeight(root.getTheChildAtIndex(i))-1) * 50.0 / 2;
                        eox = soc + ((double) root.getTheChildAtIndex(i).getSize()) / 2 * 50;
                    } else if ((double) i < ((double) root.getSize()) / 2) {
                        eox = eol - (bTree.getOrder() - 1) * (bTree.getHeight(root.getTheChildAtIndex(i))-1) * 50.0 / 2
                                - ((double) root.getTheChildAtIndex(i).getSize()) / 2 * 50;
                        soc = eox - ((double) root.getTheChildAtIndex(i).getSize()) / 2 * 50;
                    } else {
                        soc = eol - ((double) root.getTheChildAtIndex(i).getSize()) / 2 * 50;
                        eox = eol;
                    }

                    if (i == 0) {
                        soc -= 50 * 2;
                        eox -= 50 * 2;
                    } else if (i == root.getSize()) {
                        soc += 50 * 2;
                        eox += 50 * 2;
                    }

                    if (!root.getTheChildAtIndex(i).isNull()) {
                        Line line = new Line(eol, lineY, eox, y + 60);
                        line.setStroke(Color.rgb(240, 216, 122) );
                        line.setStrokeWidth(1.5);
                        this.getChildren().add(line);
                    }
                    makeBTree(root.getTheChildAtIndex(i), soc, y + 60);   // Draw child nodes
                }
            }
        }
    }
}

