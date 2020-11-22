package sample;
//remove/ add audio in these lines
//167 171, 226,231 201,205  263
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
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import Btree_JavaFx.BTree;
import Btree_JavaFx.BTPane;

import static Media.audioTest.*;
import static javafx.scene.paint.Color.*;

public class Main extends Application {

    private int verticesCount;
    private int key;
    private BTPane btPane;
    private TextField keyText = new TextField();
    private Button previousButton = new Button("Prev");
    private Button nextButton = new Button("Next");
 //Hello
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
        Button getHT = new Button(("Show Height"));
        Button getVertices = new Button(("Show Vertices"));
        insertButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        deleteButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        searchButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        nextButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        previousButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        getHT.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        getVertices.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");

        resetButton.setId("reset");
        resetButton.setStyle("-fx-background-color: red; -fx-text-fill: black;");
        Label nullLabel = new Label();
        nullLabel.setPrefWidth(30);



        //todo adjust toast
        String toastMsg = "some text...";
        int toastMsgTime = 1500; //1.5 seconds
        int fadeInTime = 500; //0.5 seconds
        int fadeOutTime= 500; //0.5 seconds
        Toast.makeText(null, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);


//todo change font type
        System.out.println(Font.getFontNames());
        Text basic = new Text("Enter a number: ");
        Font f1 = Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18);
        basic.setFill(DARKORCHID);
        basic.setFont(f1);
      /*  TextFlow tf = new TextFlow();
        Text basic2 = new Text(" B Tree ");
        basic2.setFill(DARKTURQUOISE);
        tf.setTextAlignment(TextAlignment.CENTER);
        tf.setLineSpacing(22.0);
        tf.getChildren().add(basic2);

        root.setTop(tf);*/

        hBox.getChildren().addAll(basic, keyText, insertButton, deleteButton, searchButton,
                resetButton, nullLabel,previousButton,nextButton, getHT, getVertices);
        hBox.setAlignment(Pos.CENTER);
        checker();

        // Create TreePane in center
        btPane = new BTPane(windowWidth / 2, 50, bTree);
        btPane.setPrefSize(800, 800);
        root.setCenter(btPane);
        btPane.setStyle("-fx-background-color:  #2c2f33;");
        insertButton.setOnMouseClicked(e -> insertValue());
        deleteButton.setOnMouseClicked(e -> deleteValue());
        searchButton.setOnMouseClicked(e -> findValue());
        resetButton.setOnMouseClicked(e -> reset());
        getHT.setOnMouseClicked(e-> showHt());
        getVertices.setOnMouseClicked(e-> showVertices());

        previousButton.setOnMouseClicked(e -> goPrevious());
        nextButton.setOnMouseClicked(e -> goNext());

        // Create a scene
        Scene scene = new Scene(root,  800,800);
        //todo deleted css file
        primaryStage.setTitle("Pranav & Madhav's B-Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    //todo call toast on function call
    private void showHt(){
        System.out.println("Height is: "+bTree.getHeight());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Height is: "+bTree.getHeight(), ButtonType.OK);
        alert.show();
    }

    private void showVertices(){
        System.out.println("Vertices are: "+verticesCount);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Vertices are: "+verticesCount, ButtonType.OK);
        alert.show();
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
            verticesCount++;
            index = 0;
            bTreeLinkedList = bTree.getStepTrees();
            btPane.updatePane(bTreeLinkedList.get(0));
            checker();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input!", ButtonType.OK);
            alert.show();
        }

        try {
            addedAudio();//TODO audio not audible

        } catch (NumberFormatException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
            errorAudio();
            a.show();
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
            verticesCount--;

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

        try {
            deletedAudio();//

        } catch (NumberFormatException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
            errorAudio();
            a.show();
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

        try {
            foundAudio();//TODO audio not audible

        } catch (NumberFormatException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
            errorAudio();
            a.show();
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

        restartAudio();//added audio
    }


}