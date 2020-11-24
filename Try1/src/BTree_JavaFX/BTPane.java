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

    public void MakeNode(String s, double x, double y) {//just creates a basic rectangular box and puts key in it
        Text nn = new Text(x + 18 - s.length(), y + 30, s); // Text of key value in the node
        nn.setFill(Color. rgb(20, 1, 0)  );
        nn.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 14));

        Rectangle nodeBox = new Rectangle(x, y, 50, 50); // node box configuration
        nodeBox.setFill(Color.rgb(140, 211, 255));
        nodeBox.setStroke(Color.rgb(35, 39, 42)  );
        nodeBox.setArcHeight(15); nodeBox.setArcWidth(20);

        this.getChildren().addAll(nodeBox, nn);
    }

    public void paneUpdater(BTree<Integer> bTree) {//just to reprint a new tree with updated values
        this.getChildren().clear(); // Clear all children of pane & add new ones
        this.bTree = bTree;
        makeBTree(bTree.getRoot(), X, Y); // Call to makeBTree function
    }

    public void FindNode(BTree<Integer> bTree, int key) throws Exception {
        paneUpdater(bTree);
        if (!bTree.isEmpty()) { //Check for empty tree..exit if empty
            BTree_JavaFX.BTNode<Integer> current = bTree.getRoot();//set current to point to root node at first
            double x = X, y = Y;//setting the coordinates
            double pause = 0;//adding a pause variable to pause the code for a while to make transition of finding the node more alluring
            while (!current.equals(bTree.nullBTNode)) {//iterate all the nodes
                int i = 0;
                while (i < current.getSize()) {//iterate over the keys of current node
                    colorFoundNode(current.getKey(i).toString(), x, y, pause);//call the colorFoundNOde for each key
                    pause+= 0.5;//set the duration of pause between each time as 0.5 seconds
                    if (current.getKey(i).equals(key)) { // Compare the key to be found wih current key
                        return;
                    } else if (current.getKey(i).compareTo(key) > 0) { // key < searched value then go down the tree
                        y += 60; //Coming Down
                        if ((double) i < ((double) current.getSize()) / 2) {//if the index i is less than half of the keys size of node, then set x as below
                            x = x - (bTree.getOrder() - 1) * (bTree.getHeight(current.getTheChildAtIndex(i))-1) * 50.0 / 2 - ((double) current.getTheChildAtIndex(i).getSize()) * 50; //coordinate updated
                        } else {//if the index i is more than half of the keys size of node, then update x as
                            x = x - ((double) current.getTheChildAtIndex(i).getSize()) / 2 * 50;
                        }
                        if (i == 0) {
                            x -= 50 * 2;
                        }

                        current = current.getTheChildAtIndex(i);
                        i = 0;
                    } else {//if key > searched value then go to next key in the node
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
//making a basic rectangular box and adding the key value to it
        Text nn = new Text(x + 18 - s.length(), y + 30, s);
        nn.setFill(Color.WHITE);
        nn.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 14));

        Rectangle rect = new Rectangle(x, y, 50, 50);  // Draw a node
        rect.setFill(Color.rgb(140, 211, 255));
        rect.setStroke(Color.rgb( 245, 245, 245));
        rect.setArcHeight(15); rect.setArcWidth(20);

        //adding the created box to tree's node
        this.getChildren().addAll(rect, nn);

        // make fill transition ...this makes node change its colors when it is traversed on
        FillTransition fill = new FillTransition();

        //todo understand fill
        //Setting auto reverse value to false  //The transition will set to be auto reserved by setting this to true
        //Simply it is done so that colour does not turn back to blue.
        //fill.setAutoReverse(true);

        //setting cycle count for the fill transition
        //making it 1 cause we don't want it ot change again back to blue
        fill.setCycleCount(1);
        //setting the time to be taken for transition
        fill.setDelay(Duration.seconds(delay));
        fill.setDuration(Duration.seconds(.5));

        //setting the first colour
        fill.setFromValue(Color.rgb(140, 211, 255));
        //setting the final colour
        fill.setToValue(Color.rgb(255, 206, 10));

        //adding the fill "characteristic" to rectangular box created before
        fill.setShape(rect);

        //starting the transition
        fill.play();
    }

    private void makeBTree(BTree_JavaFX.BTNode<Integer> root, double x, double y) {
        if (root != null) {//return if tree is null
            // Making keys of node...making the boxes
            for (int i = 0; i < root.getSize(); i++) {
                MakeNode(root.getKey(i).toString(), x + i * 50, y );
            }
            // Draw line...IMP: making the lines that connect the nodes
            double lineY = y + 2 * 14;//making the y coordinate of first line at centre of the root node
            if (!root.trueIfLastInternalNode()) {
                for (int i = 0; i < root.getChildren().size(); i++) {//iterating over the children of root node
                    double eol = x + i * 50; //end of Line x coordinate
                    double soc = 0, eox = 0;   // start of child nodes

                    if ((double) i > ((double) root.getSize()) / 2) {//if i i.e. the key index is more than half of the node size, then set soc=eol+((order-1)*ht from that node *difference between keys/2
                        soc = eol
                                + (bTree.getOrder() - 1) * (bTree.getHeight(root.getTheChildAtIndex(i))-1) * 50.0 / 2;
                        eox = soc + ((double) root.getTheChildAtIndex(i).getSize()) / 2 * 50;
                    } else if ((double) i < ((double) root.getSize()) / 2) {//if i i.e. the key index is more than half of node size, i.e. i is at right half of the node, then set
                        eox = eol - (bTree.getOrder() - 1) * (bTree.getHeight(root.getTheChildAtIndex(i))-1) * 50.0 / 2
                                - ((double) root.getTheChildAtIndex(i).getSize()) / 2 * 50;
                        soc = eox - ((double) root.getTheChildAtIndex(i).getSize()) / 2 * 50;
                    } else {
                        soc = eol - ((double) root.getTheChildAtIndex(i).getSize()) / 2 * 50;
                        eox = eol;
                    }

                    if (i == 0) {//whenever we get to a new node, i will be 0, there set soc and eox as follows
                        soc -= 50 * 2;
                        eox -= 50 * 2;
                    } else if (i == root.getSize()) {//if i goes beyond the last element of the node, then shift the eoc to next line
                        soc += 50 * 2;
                        eox += 50 * 2;
                    }

                    if (!root.getTheChildAtIndex(i).isNull()) {//make lines with above coordinates
                        Line line = new Line(eol, lineY, eox, y + 60);
                        line.setStroke(Color.rgb(240, 216, 122) );
                        line.setStrokeWidth(1.5);
                        this.getChildren().add(line);
                    }
                    //incrementing y by 60, going down the tree
                    makeBTree(root.getTheChildAtIndex(i), soc, y + 60);   // recursively calling the makeBTree till we reach end of tree
                }
            }
        }
    }
}

