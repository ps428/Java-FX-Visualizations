
package Media;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

import static java.lang.Thread.currentThread;
import static javafx.application.Application.launch;

public class audioTest{


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

    public static void errorAudio(){
        String addedAudioPath = "/home/pranav/Desktop/Github/PPL-M2020-Assignment/src/Media/error.mp3";
        Media addedAudioMedia = new Media(new File(addedAudioPath).toURI().toString());
        MediaPlayer addedMediaPlayer = new MediaPlayer(addedAudioMedia);
        addedMediaPlayer.setAutoPlay(true);

    }

    public static void foundAudio(){
        String addedAudioPath = "/home/pranav/Desktop/Github/PPL-M2020-Assignment/src/Media/found.mp3";
        Media addedAudioMedia = new Media(new File(addedAudioPath).toURI().toString());
        MediaPlayer addedMediaPlayer = new MediaPlayer(addedAudioMedia);
        addedMediaPlayer.setAutoPlay(true);

    }

    public static void notFoundAudio(){
        String addedAudioPath = "/home/pranav/Desktop/Github/PPL-M2020-Assignment/src/Media/notFound.mp3";
        Media addedAudioMedia = new Media(new File(addedAudioPath).toURI().toString());
        MediaPlayer addedMediaPlayer = new MediaPlayer(addedAudioMedia);
        addedMediaPlayer.setAutoPlay(true);

    }
}
