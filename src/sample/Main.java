package sample;

//Added some code in Main.java to play audio using Java FX. Will add these when a node would be inserted or deleted.

import bTreeCode.BTreeCode2;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;

import static com.sun.javafx.scene.control.skin.Utils.getResource;
import static java.lang.Thread.currentThread;

public class Main extends Application {

    //private GUIControlledBinaryHeap heap = new GUIControlledBinaryHeap();
    private BTreeCode2 BTree = new BTreeCode2(3);

    private Pane pane;
    private HBox arrayBox = new HBox();

    private boolean setup = false;

    @Override
    public void start(Stage primaryStage) throws Exception{


        Stage stage = new Stage();
        stage.setTitle("B Tree Visualization");

        pane = new Pane();
        pane.setStyle("-fx-background-color: #2C2F33;");
        pane.setPrefSize(1000,1000);

        HBox controls = new HBox();
        controls.setStyle("-fx-background-color: #23272A;");

        final TextField field = new TextField();
        Button addButton = new Button("Add to BTree");
        addButton.setStyle("-fx-background-color: #21d4ec ; ");
        addButton.setOnAction(event -> {
                    try {
                        int tmp =Integer.parseInt(field.getText());
                        BTree.Insert(tmp);
                        addedAudio();
                        primaryStage.show();
                    } catch (NumberFormatException ex) {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
                        a.show();
                    }
                }
        );

        final TextField field2 = new TextField();
        Button removeButton = new Button("Remove node");
        removeButton.setStyle("-fx-background-color: #21d4ec ; ");
        addButton.setOnAction(event -> {
                    try {
                        BTree.Remove(Integer.parseInt(field2.getText()));
                        deletedAudio();//TODO audio not audible
                        primaryStage.show();

        } catch (NumberFormatException ex) {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
                        a.show();
                    }
                }
        );

        //TODO need to search through the tree after making it
        final TextField field3 = new TextField();
        Button searchButton = new Button("Search node");
        searchButton.setStyle("-fx-background-color: #21d4ec ; ");
        addButton.setOnAction(event -> {
                    try {
                        BTree.Contain(Integer.parseInt(field2.getText()));
                    } catch (NumberFormatException ex) {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
                        a.show();
                    }
                }
        );




        controls.getChildren().addAll(field, addButton,field2, removeButton, field3, searchButton);
        controls.setSpacing(15.0);
        pane.getChildren().add(controls);

        arrayBox.setAlignment(Pos.CENTER);
        arrayBox.setSpacing(15.0);
        //arrayBox.getChildren().add(new BinaryHeapNode(-1).getArrayBox());
        pane.getChildren().add(arrayBox);




        Scene scene = new Scene(pane, 1000, 1000);
        stage.setScene(scene);

//TODO CSS for dark theme


       // scene.getStylesheets().add(this.getClass().getResource("/home/pranav/Desktop/Github/PPL-M2020-Assignment/src/Theme/Dark.css").toString());
        //fxViewObject.getStylesheets().add(this.class.getResource("/home/pranav/Desktop/Github/PPL-M2020-Assignment/src/Theme/Dark.css").toExternalForm();

        stage.show();

        arrayBox.setTranslateX(30);
        arrayBox.setTranslateY(pane.getHeight() - 60);

        //setupHeap();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void deletedAudio(){
        String deletedAudioPath = "/home/pranav/Desktop/Github/PPL-M2020-Assignment/src/Media/deleted.mp3";
        Media deletedAudioMedia = new Media(new File(deletedAudioPath).toURI().toString());
        MediaPlayer deletedMediaPlayer = new MediaPlayer(deletedAudioMedia);
        deletedMediaPlayer.setAutoPlay(true);

    }
    public static void addedAudio(){
        String addedAudioPath = "/home/pranav/Desktop/Github/PPL-M2020-Assignment/src/Media/added.mp3";
        Media addedAudioMedia = new Media(new File(addedAudioPath).toURI().toString());
        MediaPlayer addedMediaPlayer = new MediaPlayer(addedAudioMedia);
        addedMediaPlayer.setAutoPlay(true);

    }


}


