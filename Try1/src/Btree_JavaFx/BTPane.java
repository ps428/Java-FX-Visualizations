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
    private Btree_JavaFx.BTree<Integer> bTree;
    private double originalX, originalY;
    private final int fontSize = 14;
    private final int rectangleWidth = 30;
    private final int rowSpace = 60;

    public BTPane(double x, double y, BTree<Integer> bTree) {
        this.originalX = x+200;
        //Todo change location from here +200 :DONE
        this.originalY = y+200;
        this.bTree = bTree;
    }


    public void updatePane(BTree<Integer> bTree) {
        this.getChildren().clear();
        this.bTree = bTree;
        DrawBTree(bTree.getRoot(), originalX, originalY);
    }

    private void DrawNode(String s, double x, double y, Color color) {
        Rectangle node = new Rectangle(x, y, rectangleWidth, rectangleWidth);
        node.setFill(color);
        node.setStroke(Color.rgb(35, 39, 42)  );
        node.setArcHeight(10); node.setArcWidth(10);
        Text txt = new Text(x + 11 - s.length(), y + 20, s);
        txt.setFill(Color. 	rgb(20, 1, 0)  );
        txt.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, fontSize));
        this.getChildren().addAll(node, txt);
    }

    private void DrawBTree(Btree_JavaFx.BTNode<Integer> root, double x, double y) {
        if (root != null) {
            // Draw keys of node
            for (int i = 0; i < root.getSize(); i++) {
                DrawNode(root.getKey(i).toString(), x + i * rectangleWidth, y, Color.rgb(140, 211, 255) );
            }
            // Draw line
            double startY = y + 2 * fontSize;
            if (!root.isLastInternalNode()) {
                for (int i = 0; i < root.getChildren().size(); i++) {
                    double eol = x + i * rectangleWidth; //end of Line
                    double soc = 0, endX = 0;   // start of child nodes

                    if ((double) i > ((double) root.getSize()) / 2) {
                        // TODO: fix
                        soc = eol
                                + (bTree.getOrder() - 1) * (bTree.getHeight(root.getChild(i))-1) * rectangleWidth / 2;
                        endX = soc + ((double) root.getChild(i).getSize()) / 2 * rectangleWidth;
                    } else if ((double) i < ((double) root.getSize()) / 2) {
                        endX = eol - (bTree.getOrder() - 1) * (bTree.getHeight(root.getChild(i))-1) * rectangleWidth / 2
                                - ((double) root.getChild(i).getSize()) / 2 * rectangleWidth;
                        soc = endX - ((double) root.getChild(i).getSize()) / 2 * rectangleWidth;
                    } else {
                        soc = eol - ((double) root.getChild(i).getSize()) / 2 * rectangleWidth;
                        endX = eol;
                    }

                    if (i == 0) {
                        soc -= rectangleWidth * 2;
                        endX -= rectangleWidth * 2;
                    } else if (i == root.getSize()) {
                        soc += rectangleWidth * 2;
                        endX += rectangleWidth * 2;
                    }


                    if (!root.getChild(i).isNull()) {
                        Line line = new Line(eol, startY, endX, y + rowSpace);
                        line.setStroke(Color.rgb(240, 216, 122) );
                        line.setStrokeWidth(1.5);
                        this.getChildren().add(line);
                    }
                    DrawBTree(root.getChild(i), soc, y + rowSpace);   // Draw child nodes
                }
            }
        }
    }

    public void searchPathColoring(BTree<Integer> bTree, int key) throws Exception {
        updatePane(bTree);
        if (!bTree.isEmpty()) {
            Btree_JavaFx.BTNode<Integer> currentNode = bTree.getRoot();
            double x = originalX, y = originalY;
            double delay = 0;
            while (!currentNode.equals(bTree.nullBTNode)) {
                int i = 0;
                while (i < currentNode.getSize()) {
                    nodeDesign(currentNode.getKey(i).toString(), x, y, delay);
                    delay+= 1;
                    // so sanh voi key can tim
                    if (currentNode.getKey(i).equals(key)) {
                        return;
                    } else if (currentNode.getKey(i).compareTo(key) > 0) {
                        // di xuong key ben trai
                        y += rowSpace;
                        if ((double) i < ((double) currentNode.getSize()) / 2) {
                            x = x - (bTree.getOrder() - 1) * (bTree.getHeight(currentNode.getChild(i))-1) * rectangleWidth
                                    / 2 - ((double) currentNode.getChild(i).getSize()) * rectangleWidth;
                        } else {
                            x = x - ((double) currentNode.getChild(i).getSize()) / 2 * rectangleWidth;
                        }
                        if (i == 0) {
                            x -= rectangleWidth * 2;
                        }

                        currentNode = currentNode.getChild(i);
                        i = 0;
                    } else {
                        i++;  //next child
                        x += rectangleWidth;
                    }
                }

                if (!currentNode.isNull()) {
                    y += rowSpace;
                    x = x + (bTree.getOrder() - 1) * (bTree.getHeight(currentNode.getChild(i))-1) * rectangleWidth / 2
                            + rectangleWidth * 2;

                    currentNode = currentNode.getChild(currentNode.getSize());
                }
            }
        }
        throw new Exception("Not available in the tree!");
    }



    // TODO: refactor
    private void nodeDesign(String s, double x, double y, double delay) {
        // Draw a node
        Rectangle rect = new Rectangle(x, y, rectangleWidth, rectangleWidth);
        rect.setFill(Color.web("#6ab5ff"));
        rect.setStroke(Color.WHITESMOKE);
        rect.setArcHeight(10); rect.setArcWidth(10);
        Text txt = new Text(x + 11 - s.length(), y + 20, s);
        txt.setFill(Color.WHITE);
        txt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, fontSize));
        this.getChildren().addAll(rect, txt);

        // make fill transition
        FillTransition fill = new FillTransition();

        fill.setAutoReverse(false);
        fill.setCycleCount(1);
        fill.setDelay(Duration.seconds(delay));
        fill.setDuration(Duration.seconds(1));
        fill.setFromValue(Color.web("#6ab5ff"));
        fill.setToValue(Color.web("#f57f7f"));
        fill.setShape(rect);
        fill.play();
    }

}


class BTWindow extends BorderPane {
    private int windowHeight;
    private int windowWidth;


    private int key;
    private BTPane btPane;
    private TextField keyText = new TextField();
    private Button previousButton = new Button("Prev");
    private Button nextButton = new Button("Next");


    private int index = 0;
    private LinkedList<BTree<Integer>> bTreeLinkedList = new LinkedList<Btree_JavaFx.BTree<Integer>>();
    private Btree_JavaFx.BTree<Integer> bTree = new Btree_JavaFx.BTree<Integer>(3);

    public BTWindow(int windowWidth, int windowHeight) {
        super();
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
    }

    public void run() {

        HBox hBox = new HBox(15);
        this.setTop(hBox);
        BorderPane.setMargin(hBox, new Insets(10, 10, 10, 10));
        // TextField
        keyText.setPrefWidth(60);
        keyText.setAlignment(Pos.BASELINE_RIGHT);
        // Button
        Button insertButton = new Button("Insert");
        Button deleteButton = new Button("Delete");
        Button searchButton = new Button("Search");
        Button resetButton = new Button("Reset");
        resetButton.setId("reset");
        resetButton.setStyle("-fx-base: red;");
        Label nullLabel = new Label();
        nullLabel.setPrefWidth(30);

        hBox.getChildren().addAll(new Label("Enter a number: "), keyText, insertButton, deleteButton, searchButton,
                resetButton, nullLabel, previousButton, nextButton);
        hBox.setAlignment(Pos.CENTER);
        checkVisible();

        // TODO: chinh lai x, y theo size window
        btPane = new BTPane(windowWidth / 2, 50, bTree);
        btPane.setPrefSize(1000, 1000);
        this.setCenter(btPane);

        insertButton.setOnMouseClicked(e -> insertValue());
        deleteButton.setOnMouseClicked(e -> deleteValue());
        searchButton.setOnMouseClicked(e -> findValue());
        resetButton.setOnMouseClicked(e -> reset());
        previousButton.setOnMouseClicked(e -> goPrevious());
        nextButton.setOnMouseClicked(e -> goNext());
    }

    private void checkVisible() {
        if (index > 0 && index < bTreeLinkedList.size() - 1) {
            previousButton.setVisible(true);
            nextButton.setVisible(true);
        } else if (index > 0 && index == bTreeLinkedList.size() - 1) {
            previousButton.setVisible(true);
            nextButton.setVisible(false);
        } else if (index == 0 && index < bTreeLinkedList.size() - 1) {
            previousButton.setVisible(false);
            nextButton.setVisible(true);
        } else {
            previousButton.setVisible(false);
            nextButton.setVisible(false);
        }
    }

    private void insertValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");
            bTree.setStepTrees(new LinkedList<Btree_JavaFx.BTree<Integer>>());

            bTree.insert(key);

            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.updatePane(bTreeLinkedList.get(0));
            checkVisible();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input data!", ButtonType.OK);
            alert.show();
        }
    }

    private void findValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");

            btPane.searchPathColoring(bTree, key);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input data!", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }


    private void deleteValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");
            if (bTree.getNode(key) == bTree.nullBTNode) {
                throw new Exception("Not in the tree!");
            }
            bTree.setStepTrees(new LinkedList<Btree_JavaFx.BTree<Integer>>());

            bTree.delete(key);

            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.updatePane(bTreeLinkedList.get(0));
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
            btPane.updatePane(bTreeLinkedList.get(index));

            checkVisible();
        }
    }

    private void goNext() {
        if (index < bTreeLinkedList.size() - 1) {
            index++;
            System.out.println("index: " + index + " - size: " + bTreeLinkedList.size());
            btPane.updatePane(bTreeLinkedList.get(index));

            checkVisible();
        }
    }

    private void reset() {
        keyText.setText("");

        bTree.setRoot(null);
        index = 0;
        bTreeLinkedList.clear();
        btPane.updatePane(bTree);
        checkVisible();
    }

}

