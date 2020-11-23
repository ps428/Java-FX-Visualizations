package sample;
//remove/ add audio in these lines
//155 164 180 190 194 205 209 213 243
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javafx.animation.PauseTransition;
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
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import Btree_JavaFx.BTree;
import Btree_JavaFx.BTPane;
import javafx.util.Duration;

import static Media.audioTest.*;
import static javafx.scene.paint.Color.*;

public class Main extends Application {

     int verticesCount;
     int key;
     BTPane btPane;
     TextField keyText = new TextField();


    Button antecedent = new Button("Revert");
    Button subsequent = new Button("Transform");
 //Hello
     int index = 0;
     LinkedList<BTree<Integer>> bTreeLinkedList = new LinkedList<BTree<Integer>>();
     BTree<Integer> bTree = new BTree<Integer>(3);

    @Override
    public void start(Stage primaryStage) {
        final int windowWidth = 720;

        BorderPane root = new BorderPane();

        // Create button HBox on top
        HBox hBox = new HBox(15);
        root.setBottom(hBox);
        BorderPane.setMargin(hBox, new Insets(10, 10, 10, 10));
        // TextField
        keyText.setPrefWidth(60);
        keyText.setAlignment(Pos.BASELINE_RIGHT);
        // Buttons
        Button insertButton = new Button("Insert");
        Button deleteButton = new Button("Delete");
        Button searchButton = new Button("Find");
        Button resetButton = new Button("Reset");
        Button getHT = new Button(("Show Height"));
        Button getVertices = new Button(("Show Vertices"));

        insertButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        deleteButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        searchButton.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        subsequent.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
        antecedent.setStyle( "-fx-background-color:  #7289da ; -fx-text-fill:  	#ffffff ; ");
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


        //System.out.println(Font.getFontNames());
        Text basic = new Text("Enter a number: ");
        Font f1 = Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 18);
        basic.setFill(DARKORCHID);
        basic.setFont(f1);

        hBox.getChildren().addAll(basic, keyText, insertButton, deleteButton, searchButton,
                resetButton, nullLabel, antecedent, subsequent, getHT, getVertices);
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

        antecedent.setOnMouseClicked(e -> antecedent());
        subsequent.setOnMouseClicked(e -> subsequent());

        // Create a scene
        Scene scene = new Scene(root,  800,800);
        primaryStage.setTitle("Pranav & Madhav's B-Tree Visualization");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    void showNotification(String inp){
        Alert notification = new Alert(Alert.AlertType.INFORMATION, inp, ButtonType.CLOSE);
        notification.setX(1200);
        notification.setY(700);
        notification.setHeaderText("Notification: Task Successful");
        notification.show();
//todo add notifications here...add close automatically part
       /* try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        notification.close();*/

        /* Stage stage1 = new Stage();
        stage1.setTitle("Creating popup");

        Label label =new Label(inp);
        label.setStyle(" -fx-background-color: white;");
        Popup popup = new Popup();
        popup.getContent().add(label);
        popup.setX(1200);
        popup.setY(700);
        popup.show(stage1);
        stage1.show();

        try {
            Thread.sleep(1200);
            stage1.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }
    //todo call toast on function call: On Hold... added alert notification instead
     void showHt(){
        System.out.println("Height is: "+bTree.getHeight());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Height is: "+bTree.getHeight(), ButtonType.OK);
        alert.show();
    }

     void showVertices(){
        System.out.println("Vertices are: "+verticesCount);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Vertices are: "+verticesCount, ButtonType.OK);
        alert.show();
    }

     void checker() {//todo check index wala this later..check when is previous and next button visible
        if (index > 0 && index < bTreeLinkedList.size() - 1) {//show antecedent and precedent buttons when,
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

     void insertValue() {
        try {
            key = Integer.parseInt(keyText.getText());//getting the key from text field
            keyText.setText("");//removing the number from text field after adding it

            bTree.setStTrees(new LinkedList<BTree<Integer>>());

            bTree.insert(key);//inserting to tree
            addedAudio();//audio:  audible
            showNotification("Inserted: "+key);
            verticesCount++;
            index = 0;
            bTreeLinkedList = bTree.getStTrees();
            btPane.paneUpdater(bTreeLinkedList.get(0));//re printing the tree
            checker();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input!", ButtonType.OK);
            errorAudio();
            alert.show();
        }
    }

     void deleteValue() {
        try {
            key = Integer.parseInt(keyText.getText());//getting the key from text field
            keyText.setText("");//removing the number from text field after adding it

            if (bTree.getNode(key) == bTree.nullBTNode) {//throw error if the node is not present in the tree
                throw new Exception("Node is not present in the tree!");
            }
            bTree.setStTrees(new LinkedList<BTree<Integer>>());

            bTree.delete(key);//removing from the tree
            deletedAudio();//playing audio
            showNotification("Removed: "+key);

            verticesCount--;

            index = 0;
            bTreeLinkedList = bTree.getStTrees();
            btPane.paneUpdater(bTreeLinkedList.get(0));//reprinting the tree
            checker();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input!", ButtonType.OK);
            errorAudio();
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            errorAudio();
            alert.show();
        }
    }

     void findValue() {
        try {
            key = Integer.parseInt(keyText.getText());//getting the key from text field
            keyText.setText("");//removing the number from text field after adding it

            btPane.FindNode(bTree, key);//checking the node in the tree
            foundAudio();//playing audio
            showNotification("Found: "+key);


        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Illegal input!", ButtonType.OK);
            errorAudio();
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage(), ButtonType.OK);
            errorAudio();
            alert.show();
        }
    }

     void antecedent() {
        if (index > 0) {
            index--;
            btPane.paneUpdater(bTreeLinkedList.get(index));//getting to past screen/ reprinting the old tree again
            checker();
        }
    }

     void subsequent() {
        if (index < bTreeLinkedList.size() - 1) {//incrementing index till it is equal to max number of keys that a node can hold
            index++;
            btPane.paneUpdater(bTreeLinkedList.get(index));//getting to next screen/ printing the new tree
            checker();
        }
    }

     void reset() {
        keyText.setText("");//emptying the text field

        bTree.setRoot(null);//setting root to null
        index = 0;
        bTreeLinkedList.clear();//emptying the linked list of trees
        btPane.paneUpdater(bTree);//reprint the tree//here, it will be an empty screen as root node is empty
        checker();

        restartAudio();//added audio
    }


}