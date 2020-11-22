package sample;

import java.util.LinkedList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import Btree_JavaFx.BTree;
import Btree_JavaFx.BTreePane;

import javax.xml.soap.Text;

import static javafx.scene.paint.Color.rgb;

public class Main extends Application {

    private int key;
    private BTreePane btPane;
    private TextField keyText = new TextField();
    private Button previousButton = new Button("Prev");
    private Button nextButton = new Button("Next");

    private int index = 0;
    private LinkedList<BTree<Integer>> bTreeLinkedList = new LinkedList<BTree<Integer>>();
    private BTree<Integer> bTree = new BTree<Integer>(3);

    @Override
    public void start(Stage primaryStage) {
        // TODO Auto-generated method stub
        final int windowHeight = 480;
        final int windowWidth = 720;

        BorderPane root = new BorderPane();

        // Create button HBox on top
        HBox hBox = new HBox(15);
        root.setBottom(hBox);
        BorderPane.setMargin(hBox, new Insets(10, 10, 10, 10));
        // TextField
        keyText.setPrefWidth(60);
        keyText.setAlignment(Pos.BASELINE_RIGHT);
        // Button
        Button insertButton = new Button("Insert");
        Button deleteButton = new Button("Delete");
        Button searchButton = new Button("Find");
        Button resetButton = new Button("Reset");
        insertButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        deleteButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        searchButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        nextButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        previousButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");

        resetButton.setId("reset");
        resetButton.setStyle("-fx-base: red; -fx-text-fill: black;");
        Label nullLabel = new Label();
        nullLabel.setPrefWidth(30);

        hBox.getChildren().addAll(new Label("Enter a number: "), keyText, insertButton, deleteButton, searchButton,
                resetButton, nullLabel,previousButton,nextButton, new Label("Height: "), new Label(" Vertices: "));
        hBox.setAlignment(Pos.CENTER);
        checker();

        // Create TreePane in center
        btPane = new BTreePane(windowWidth / 2, 50, bTree);
        btPane.setPrefSize(1000, 1000);
        root.setCenter(btPane);
        btPane.setStyle("-fx-background-color:  #2c2f33;");
        insertButton.setOnMouseClicked(e -> insertValue());
        deleteButton.setOnMouseClicked(e -> deleteValue());
        searchButton.setOnMouseClicked(e -> findValue());
        resetButton.setOnMouseClicked(e -> reset());
        previousButton.setOnMouseClicked(e -> goPrevious());
        nextButton.setOnMouseClicked(e -> goNext());

        // Create a scene
        Scene scene = new Scene(root,  1000,1000);
        //todo deleted css file
        primaryStage.setTitle("Pranav & Madhav's B-Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void checker() {
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
            bTree.setStepTrees(new LinkedList<BTree<Integer>>());

            bTree.insert(key);

            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.updatePane(bTreeLinkedList.get(0));
            checker();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input!", ButtonType.OK);
            alert.show();
        }
    }

    private void deleteValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");
            if (bTree.getNode(key) == bTree.nullBTNode) {
                throw new Exception("Value not found in the tree!");
            }
            bTree.setStepTrees(new LinkedList<BTree<Integer>>());

            bTree.delete(key);

            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.updatePane(bTreeLinkedList.get(0));
            checker();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input!", ButtonType.OK);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.show();
        }
    }

    private void findValue() {
        try {
            key = Integer.parseInt(keyText.getText());
            keyText.setText("");

            btPane.searchPathColoring(bTree, key);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input!", ButtonType.OK);
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

            checker();
        }
    }

    private void goNext() {
        if (index < bTreeLinkedList.size() - 1) {
            index++;
            System.out.println("index: " + index + " - size: " + bTreeLinkedList.size());
            btPane.updatePane(bTreeLinkedList.get(index));

            checker();
        }
    }

    private void reset() {
        keyText.setText("");

        bTree.setRoot(null);
        index = 0;
        bTreeLinkedList.clear();
        btPane.updatePane(bTree);
        checker();
    }
}