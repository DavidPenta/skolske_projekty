package com.musicverse.client.collections.itemActions;

import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.api.ServerAPI;
import com.musicverse.client.objects.Album;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class AlbumActionDropDown implements ItemActionDropDown<Album>{

    private ServerAPI api;

    private final int[] idOfLastSong = new int[1];
    private final int[] duration = new int[1];
    private final File[] selectedFile = new File[1];

    private Album item;

    private Menu menu = new javafx.scene.control.Menu("Selected album action");
    private javafx.scene.control.MenuItem deleteAlbum = new javafx.scene.control.MenuItem("Delete album");
    private javafx.scene.control.MenuItem addToAlbum = new javafx.scene.control.MenuItem("Add to album");
    private javafx.scene.control.MenuItem openAlbum = new javafx.scene.control.MenuItem("Open album");
    private javafx.scene.control.MenuItem albumDescription = new javafx.scene.control.MenuItem("Album description");
    @Override
    public Menu setMenu(Album item, AnchorPane pane, int id, int selectedId, ActionEvent event) {

        menu.getItems().add(openAlbum);
        menu.getItems().add(albumDescription);
        menu.getItems().add(addToAlbum);
        menu.getItems().add(deleteAlbum);

        this.item = item;

        this.api = ServerAPI.getInstance();

        if (id > 0){
            addToAlbum.setVisible(false);
            deleteAlbum.setVisible(false);
        }

        openAlbum.setOnAction(e -> {

            InitScreensFunctions initScreensFunctions = new InitScreensFunctions();

            try {
                initScreensFunctions.initSettingsScreen("Artist", "/ArtistSectionScreen.fxml",
                        pane, id, Integer.parseInt(item.getId()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        albumDescription.setOnAction(e -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            dialog.setHeight(400);

            Button buttonCancel = new Button("Cancel");

            dialogVbox.getChildren().add(new Text("Album description"));

            dialogVbox.getChildren().add(new Text(item.getDescription()));

            dialogVbox.getChildren().add(buttonCancel);

            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();

            buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dialog.close();
                }
            });
        });

        addToAlbum.setOnAction(e -> {

            Stage dialog2 = new Stage();
            dialog2.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            dialog2.setHeight(400);

            FileChooser fileChooser = new FileChooser();
            Button button = new Button("Select File");
            button.setOnAction(l -> {
                selectedFile[0] = fileChooser.showOpenDialog(dialog2);
                try {
                    duration[0] = getDuration(selectedFile[0]);
                } catch (UnsupportedAudioFileException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                dialog2.close();
                songUpload();
            });

            VBox dialogVbox2 = new VBox(button);
            Scene dialogScene2 = new Scene(dialogVbox2, 960, 600);
            dialog2.setScene(dialogScene2);
            dialog2.show();

        });

        deleteAlbum.setOnAction(e -> {

            api.deleteCollection(Integer.parseInt(item.getId()), 1);
            InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
            try {
                initScreensFunctions.initSettingsScreen("Artist", "/ArtistSectionScreen.fxml",
                        pane, id, 0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return menu;
    }

    private static int getDuration(File file) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        int seconds = 0;
        if (fileFormat instanceof TAudioFileFormat) {
            Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
            String key = "duration";
            Long microseconds = (Long) properties.get(key);
            int mili = (int) (microseconds / 1000);
            int sec = (mili / 1000) % 60;
            int min = (mili / 1000) / 60;
            seconds = (min * 60) + sec;
        }
        return seconds;
    }

    private void songUpload(){
        final Stage[] dialog = {new Stage()};
        dialog[0].initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialog[0].setHeight(500);

        TextField nameField = new TextField("Song name");
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");

        dialogVbox.getChildren().add(new Text("Enter song name"));
        dialogVbox.getChildren().add(nameField);

        dialogVbox.getChildren().add(buttonSave);
        dialogVbox.getChildren().add(buttonCancel);

        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog[0].setScene(dialogScene);
        dialog[0].show();

        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog[0].close();
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                idOfLastSong[0] = api.createSong(
                        nameField.getText(),
                        Integer.parseInt(item.getArtist_id()),
                        Integer.parseInt(item.getId()),
                        Integer.parseInt(item.getGenre_id()),
                        duration[0]
                );
                try {
                    api.uploadSongData(selectedFile[0], idOfLastSong[0]);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dialog[0].close();
            }
        });
    }


}
