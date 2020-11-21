
package Media;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

import static java.lang.Thread.currentThread;
import static javafx.application.Application.launch;

public class audioTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //
        //Initialising path of the media file, replace this with your file path
      String deletedAudioPath = "/home/pranav/Desktop/Github/PPL-M2020-Assignment/src/Media/deleted.mp3";
        String addedAudioPath = "/home/pranav/Desktop/Github/PPL-M2020-Assignment/src/Media/added.mp3";

        //Instantiating Media class
        Media deletedAudioMedia = new Media(new File(deletedAudioPath).toURI().toString());
        Media addedAudioMedia = new Media(new File(addedAudioPath).toURI().toString());

        //Instantiating MediaPlayer class
        MediaPlayer deletedMediaPlayer = new MediaPlayer(deletedAudioMedia);
        MediaPlayer addedMediaPlayer = new MediaPlayer(addedAudioMedia);

        //by setting this property to true, the audio will be played

        currentThread().sleep(2000);
        deletedMediaPlayer.setAutoPlay(true);
        primaryStage.setTitle("Playing Audio");

        currentThread().sleep(2000);
        addedMediaPlayer.setAutoPlay(true);
        primaryStage.setTitle("Playing Audio");
        primaryStage.show();}


    public static void main(String[] args) {
        launch(args);
    }
}
