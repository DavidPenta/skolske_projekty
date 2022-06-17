package com.musicverse.client.collections.itemActions;

import com.falsepattern.json.node.JsonNode;
import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.api.ServerAPI;
import com.musicverse.client.objects.Song;
import com.musicverse.client.player.MediaManager;
import com.musicverse.client.player.MusicPlayerException;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.val;

import java.io.IOException;
import java.util.ArrayList;

public class SongActionDropDown implements ItemActionDropDown<Song>{

    private Menu menu = new javafx.scene.control.Menu("Selected song action");
    private javafx.scene.control.MenuItem deleteSong = new javafx.scene.control.MenuItem("Delete song");
    private javafx.scene.control.MenuItem addToPlaylist = new javafx.scene.control.MenuItem("Add to playlist");
    private javafx.scene.control.MenuItem playSong = new javafx.scene.control.MenuItem("Play song");

    private ServerAPI api;

    @Override
    public Menu setMenu(Song song, AnchorPane pane, int id, int selectedId, ActionEvent event) {

        menu.getItems().add(playSong);
        menu.getItems().add(addToPlaylist);
        menu.getItems().add(deleteSong);

        this.api = ServerAPI.getInstance();
        if (id > 0){
            deleteSong.setVisible(false);
        }
        if (PreferencesLogin.getPrefs().getId() < 1){
            addToPlaylist.setVisible(false);
        }
        //verify user for deleting song from artist
        if (id == 0){
            deleteSong.setVisible(true);
            /*JsonNode artist = api.loadArtist(PreferencesLogin.getPrefs().getId(), 0);
            if (artist != null){
                if (!(artist.getInt("user_id") == PreferencesLogin.getPrefs().getId())){
                    deleteSong.setVisible(false);
                }
            } else {
                deleteSong.setVisible(false);
            }*/
        }

        playSong.setOnAction(e -> {
                // TODO songs playing code
           /* try {
                MediaManager.instance.setCurrentSong(1);
            } catch (MusicPlayerException ex) {
                throw new RuntimeException(ex);
            }
            val audioPlayer = MediaManager.instance.getPlayer();
            try {
                audioPlayer.play();
            } catch (MusicPlayerException ex) {
                throw new RuntimeException(ex);
            }*/
            InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
            try {
                initScreensFunctions.initPlayerSscreen(song);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        //only for songs reached from artist, it means id = 1
        addToPlaylist.setOnAction(e -> {

            JsonNode jn = api.getUserPlaylists(PreferencesLogin.getPrefs().getId());
            ArrayList<String> playlists = new ArrayList<String>();
            for (int i = 0; i < jn.size(); i++) {
                playlists.add(jn.get(i).getString("name"));
            }

            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            dialog.setHeight(500);

            Button buttonSave = new Button("Save");
            Button buttonCancel = new Button("Cancel");

            dialogVbox.getChildren().add(new Text("Choose playlist"));

            ComboBox combo_box =
                    new ComboBox(FXCollections
                            .observableArrayList(playlists));
            dialogVbox.getChildren().add(combo_box);

            dialogVbox.getChildren().add(buttonSave);
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

            buttonSave.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int playlistId;
                   for (int i = 0; i < jn.size(); i++){
                       if (jn.get(i).getString("name") == combo_box.getSelectionModel().getSelectedItem()){
                           playlistId = jn.get(i).getInt("id");
                           api.addToPlaylist(playlistId, Integer.parseInt(song.getId()));
                           api.downloadSongData(Integer.parseInt(song.getId()));
                           dialog.close();
                       }
                   }
                }
            });

        });

        deleteSong.setOnAction(e -> {
            System.out.println(song.getId());
            int songId = Integer.parseInt(song.getId());
            int playlistId = selectedId;
            if(api.deleteSong(songId, playlistId, id))
            {
                InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
                try {
                    initScreensFunctions.initMainScreen(event);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else{
                //TODO else condition in failure
            }

        });



        return menu;
    }
}
