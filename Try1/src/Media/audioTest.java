
package Media;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

import static java.lang.Thread.currentThread;
import static javafx.application.Application.launch;

public class audioTest{

    // For Ubuntu": PRANAV
    private static String location = "/home/pranav/Desktop/Github/PPL-M2020-Assignment/Try1/src/Media/";//location to Pranav's folder of mp3 files;

    // For Windows: MADDY
    // private static String location = "C:\\Users\\Mahav\\Desktop\\ppl_labs\\New\\PPL-M2020-Assignment\\Try1\\src\\Media\\";//location to  Madhav's folder of mp3 files;

    public static void deletedAudio(){
        String deletedAudioPath = location+"deleted.mp3";
        Media deletedAudioMedia = new Media(new File(deletedAudioPath).toURI().toString());
        MediaPlayer deletedMediaPlayer = new MediaPlayer(deletedAudioMedia);
        deletedMediaPlayer.setAutoPlay(true);

    }
    public static void addedAudio(){
        String addedAudioPath = location+"added.mp3";
        Media addedAudioMedia = new Media(new File(addedAudioPath).toURI().toString());
        MediaPlayer addedMediaPlayer = new MediaPlayer(addedAudioMedia);
        addedMediaPlayer.setAutoPlay(true);

    }

    public static void errorAudio(){
        String addedAudioPath = location+"error.mp3";
        Media addedAudioMedia = new Media(new File(addedAudioPath).toURI().toString());
        MediaPlayer addedMediaPlayer = new MediaPlayer(addedAudioMedia);
        addedMediaPlayer.setAutoPlay(true);

    }

    public static void foundAudio(){
        String addedAudioPath = location+"found.mp3";
        Media addedAudioMedia = new Media(new File(addedAudioPath).toURI().toString());
        MediaPlayer addedMediaPlayer = new MediaPlayer(addedAudioMedia);
        addedMediaPlayer.setAutoPlay(true);

    }

    public static void restartAudio(){
        String addedAudioPath = location+"restart.mp3";
        Media addedAudioMedia = new Media(new File(addedAudioPath).toURI().toString());
        MediaPlayer addedMediaPlayer = new MediaPlayer(addedAudioMedia);
        addedMediaPlayer.setAutoPlay(true);

    }
}
