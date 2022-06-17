package com.musicverse.client.collections;

import com.falsepattern.json.node.JsonNode;
import com.musicverse.client.objects.Song;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Utils {
    /**
     * metoda na vygenerovanie hash mapy ktora je vlozena do ListView playlistov
     * @param playlists je zoznam playlistov ktore chceme vlozit do hashmapy
     * @return vratime hashmapu naplnenu playlistami a ich ID
     */
    public static HashMap<Integer,String> createPlaylist(JsonNode playlists){
        HashMap<Integer,String> list = new HashMap<>();
        for (int i = 0; i<playlists.size();i++) {
            list.put(playlists.get(i).getInt("id"),playlists.get(i).getString("name"));
        }
        return list;
    }

    /**
     * metoda na naplenenie arraylistu pesnickami a nasledne je arraylist zaslany na naplnenie tableView pomocou
     * class Controller
     * @param tableView ktory chceme naplnit
     * @param songs2 data, ktorymi chceme plnit
     * @param mainPane vlastnik stage
     * @return vyplneny tableView
     */
    public TableView<Song> generateTableSongs(TableView<Song> tableView, JsonNode songs2, AnchorPane mainPane){
        ArrayList<Song> songArrayList = new ArrayList<>();

        for (int i = 0; i < songs2.size(); i++){
            songArrayList.add(
                    new Song(
                            String.valueOf(songs2.get(i).getInt("album_id")),
                            songs2.get(i).getString("data"),
                            songs2.get(i).getString("description"),
                            songs2.get(i).getString("name"),
                            String.valueOf(songs2.get(i).getString("album")),
                            String.valueOf(songs2.get(i).getString("artist")),
                            String.valueOf(songs2.get(i).getInt("duration")),
                            songs2.get(i).getString("genre"),
                            String.valueOf(songs2.get(i).getInt("id")),
                            String.valueOf(songs2.get(i).getInt("artist_id")),
                            String.valueOf(songs2.get(i).getInt("genre_id"))
                    )
            );
        }
        if (tableView != null){
            tableView.getColumns().clear();
            tableView.getItems().clear();
        }

        Controller<Song> controller = new Controller<>();
        tableView = controller.initialize(
                songArrayList,
                new String[]{"album_id", "data", "description",
                        "name", "album", "artist","duration","genre", "id",  "artist_id", "genre_id"},
                tableView, 1, mainPane
        );
        tableView.setVisible(true);
        tableView.refresh();
        return tableView;
    }

}
