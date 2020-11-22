package Btree_JavaFx;

import javafx.animation.FillTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.LinkedList;

public class BTPane extends Pane {
    Btree_JavaFx.BTree<Integer> bTree; // object of class Btree
    double X, Y;   // coordinates

    public BTPane(double x, double y, BTree<Integer> bTree) {
        this.X = x+200;
        this.Y = y+200;
        this.bTree = bTree;
    }




    public void MakeNode(String s, double x, double y, Color color) {
        Text nn = new Text(x + 18 - s.length(), y + 30, s); // Text of key value in the node
        nn.setFill(Color. 	rgb(20, 1, 0)  );
        nn.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 14));

        Rectangle nodeBox = new Rectangle(x, y, 50, 50); // node box configuration
        nodeBox.setFill(color);
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
            Btree_JavaFx.BTNode<Integer> current = bTree.getRoot();
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
                            x = x - (bTree.getOrder() - 1) * (bTree.getHeight(current.getChild(i))-1) * 50.0 / 2 - ((double) current.getChild(i).getSize()) * 50; //coordinate updated
                        } else {
                            x = x - ((double) current.getChild(i).getSize()) / 2 * 50;
                        }
                        if (i == 0) {
                            x -= 50 * 2;
                        }

                        current = current.getChild(i);
                        i = 0;
                    } else {
                        i++;  //next child
                        x += 50;
                    }
                }

                if (!current.isNull()) {
                    y += 60;
                    x = x + (bTree.getOrder() - 1) * (bTree.getHeight(current.getChild(i))-1) * 50.0 / 2 + 50 * 2; // coordinate updated

                    current = current.getChild(current.getSize());
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
        rect.setFill(Color.web("#6ab5ff"));
        rect.setStroke(Color.WHITESMOKE);
        rect.setArcHeight(15); rect.setArcWidth(20);

        this.getChildren().addAll(rect, nn);

        // make fill transition
        FillTransition fill = new FillTransition();

        fill.setAutoReverse(false);
        fill.setCycleCount(1);
        fill.setDelay(Duration.seconds(delay));
        fill.setDuration(Duration.seconds(1));
        fill.setFromValue(Color.web("#6ab5ff")); //Todo color search node
        fill.setToValue(Color.web("#f57f7f"));
        fill.setShape(rect);
        fill.play();
    }

    private void makeBTree(Btree_JavaFx.BTNode<Integer> root, double x, double y) { //Todo check
        if (root != null) {
            // Making keys of node
            for (int i = 0; i < root.getSize(); i++) {
                MakeNode(root.getKey(i).toString(), x + i * 50, y, Color.rgb(140, 211, 255) );
            }
            // Draw line
            double lineY = y + 2 * 14;
            if (!root.isLastInternalNode()) {
                for (int i = 0; i < root.getChildren().size(); i++) {
                    double eol = x + i * 50; //end of Line
                    double soc = 0, eox = 0;   // start of child nodes

                    if ((double) i > ((double) root.getSize()) / 2) {
                        // TODO: fix
                        soc = eol
                                + (bTree.getOrder() - 1) * (bTree.getHeight(root.getChild(i))-1) * 50.0 / 2;
                        eox = soc + ((double) root.getChild(i).getSize()) / 2 * 50;
                    } else if ((double) i < ((double) root.getSize()) / 2) {
                        eox = eol - (bTree.getOrder() - 1) * (bTree.getHeight(root.getChild(i))-1) * 50.0 / 2
                                - ((double) root.getChild(i).getSize()) / 2 * 50;
                        soc = eox - ((double) root.getChild(i).getSize()) / 2 * 50;
                    } else {
                        soc = eol - ((double) root.getChild(i).getSize()) / 2 * 50;
                        eox = eol;
                    }

                    if (i == 0) {
                        soc -= 50 * 2;
                        eox -= 50 * 2;
                    } else if (i == root.getSize()) {
                        soc += 50 * 2;
                        eox += 50 * 2;
                    }


                    if (!root.getChild(i).isNull()) {
                        Line line = new Line(eol, lineY, eox, y + 60);
                        line.setStroke(Color.rgb(240, 216, 122) );
                        line.setStrokeWidth(1.5);
                        this.getChildren().add(line);
                    }
                    makeBTree(root.getChild(i), soc, y + 60);   // Draw child nodes
                }
            }
        }
    }



}


class BTWin extends BorderPane {
    int windowHeight;
    int windowWidth;

    public BTWin(int windowWidth, int windowHeight) {
        super();
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
    }

    int key;
    BTPane btPane;
    TextField inputTxt = new TextField();
     Button antecedent = new Button("Antecedent");
     Button subsequent = new Button("subsequent");


     int index = 0;
     LinkedList<BTree<Integer>> bTreeLinkedList = new LinkedList<Btree_JavaFx.BTree<Integer>>();
     Btree_JavaFx.BTree<Integer> bTree = new Btree_JavaFx.BTree<Integer>(3);



    public void runner() {

        HBox horizontalBox = new HBox(15);
        this.setTop(horizontalBox);
        BorderPane.setMargin(horizontalBox, new Insets(10, 10, 10, 10));

        // Buttons
        Button insertButton = new Button("Insert");
        Button deleteButton = new Button("Delete");
        Button searchButton = new Button("Search");
        Button resetButton = new Button("Reset");
        resetButton.setId("reset");
        resetButton.setStyle("-fx-base: red;");
        Label nullLabel = new Label();
        nullLabel.setPrefWidth(30);

        inputTxt.setPrefWidth(60);
        inputTxt.setAlignment(Pos.BASELINE_RIGHT); //Todo Update run func

        horizontalBox.getChildren().addAll(new Label("Enter a number: "), inputTxt, insertButton, deleteButton, searchButton,
                resetButton, nullLabel, antecedent, subsequent);
        horizontalBox.setAlignment(Pos.CENTER);
        checkVisible();


        btPane = new BTPane(windowWidth / 2.0, 50, bTree);
        btPane.setPrefSize(1000, 1000);
        this.setCenter(btPane);

        insertButton.setOnMouseClicked(e -> insertion());  // Mouse click events
        deleteButton.setOnMouseClicked(e -> deleteValue());
        searchButton.setOnMouseClicked(e -> findValue());
        resetButton.setOnMouseClicked(e -> reset());
        antecedent.setOnMouseClicked(e -> goPrevious());
        subsequent.setOnMouseClicked(e -> goNext());
    }



    void insertion() {
        try {
            key = Integer.parseInt(inputTxt.getText());
            inputTxt.setText("");
            bTree.setStepTrees(new LinkedList<Btree_JavaFx.BTree<Integer>>());

            bTree.insert(key);

            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.paneUpdater(bTreeLinkedList.get(0));
            checkVisible();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input !", ButtonType.OK);
            alert.show();
        }
    }

    private void checkVisible() {
        if (index > 0 && index < bTreeLinkedList.size() - 1) {
            antecedent.setVisible(true);
            subsequent.setVisible(true);
        } else if (index > 0 && index == bTreeLinkedList.size() - 1) {
            antecedent.setVisible(true);
            subsequent.setVisible(false);
        } else if (index == 0 && index < bTreeLinkedList.size() - 1) {
            antecedent.setVisible(false);
            subsequent.setVisible(true);
        } else {
            antecedent.setVisible(false);
            subsequent.setVisible(false);
        }
    }

    private void findValue() {
        try {
            key = Integer.parseInt(inputTxt.getText());
            inputTxt.setText("");

            btPane.FindNode(bTree, key);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input!", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }


    private void deleteValue() {
        try {
            key = Integer.parseInt(inputTxt.getText());
            inputTxt.setText("");
            if (bTree.getNode(key) == bTree.nullBTNode) {
                throw new Exception("Not in the tree!");
            }
            bTree.setStepTrees(new LinkedList<Btree_JavaFx.BTree<Integer>>());

            bTree.delete(key);

            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.paneUpdater(bTreeLinkedList.get(0));
            checkVisible();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input data!", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }



    private void goPrevious() {
        if (index > 0) {
            index--;
            btPane.paneUpdater(bTreeLinkedList.get(index));

            checkVisible();
        }
    }

    private void goNext() {
        if (index < bTreeLinkedList.size() - 1) {
            index++;
            System.out.println("index: " + index + " - size: " + bTreeLinkedList.size());
            btPane.paneUpdater(bTreeLinkedList.get(index));

            checkVisible();
        }
    }

    private void reset() {
        inputTxt.setText("");

        bTree.setRoot(null);
        index = 0;
        bTreeLinkedList.clear();
        btPane.paneUpdater(bTree);
        checkVisible();
    }

}

