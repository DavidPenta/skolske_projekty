package com.musicverse.client.gui;

import com.falsepattern.json.node.JsonNode;
import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.api.ServerAPI;
import com.musicverse.client.collections.Controller;
import com.musicverse.client.collections.Utils;
import com.musicverse.client.collections.itemActions.AlbumActionDropDown;
import com.musicverse.client.collections.itemActions.SongActionDropDown;
import com.musicverse.client.objects.Album;
import com.musicverse.client.objects.Artist;
import com.musicverse.client.objects.Song;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;
import lombok.val;

import java.util.ArrayList;

public class SearchScreen {

    @FXML
    private MenuBar albumAction;

    @FXML
    private Button albumsBtn;

    @FXML
    private MenuBar artistAction;

    @FXML
    private Button artistsBtn;

    private ServerAPI api;

    @FXML
    private TableView<Artist> artistsTable;

    @FXML
    private Label backLabel;

    @FXML
    private Label labelSearch;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private MenuBar songAction;

    @FXML
    private Button songsBtn;

    @FXML
    private TableView<Album> tableAlbums;

    @FXML
    private TableView<Song> tableSongs;

    @FXML
    private Label title;

    private String regex;

    private Song song;

    private Artist artist;

    @FXML
    private AnchorPane mainPane;

    private Album album;

    private JsonNode albums;

    @FXML
    void onAlbumBtnClick(ActionEvent event) {
        tableAlbums.setVisible(true);
        tableSongs.setVisible(false);
        artistsTable.setVisible(false);
        artistAction.setVisible(false);
        songAction.setVisible(false);
        labelSearch.setText("Albums");
        labelSearch.setVisible(true);

        val api = ServerAPI.getInstance();
        albums = api.loadAlbumsRegex(regex);

        ArrayList<Album> albumArrayList = new ArrayList<>();
        //String id, String name, String description, String artist_id, String genre_id, String artist, String genre
        for (int i = 0; i < albums.size(); i++){
            albumArrayList.add(
                    new Album(
                            String.valueOf(albums.get(i).getInt("id")),
                            albums.get(i).getString("name"),
                            albums.get(i).getString("description"),
                            String.valueOf(albums.get(i).getInt("artist_id")),
                            "0",
                            albums.get(i).getString("artist"),
                            "n"
                    )

            );
        }
        Controller<Album> controller = new Controller<>();
        tableAlbums.getItems().clear();
        tableAlbums = controller.initialize(albumArrayList, new String[]{"id", "name", "description", "artist_id", "genre_id", "artist", "genre"}, tableAlbums, 2, mainPane);
        tableAlbums.setVisible(true);
        tableAlbums.refresh();
    }

    @FXML
    void onAlbumItemClicked(MouseEvent event) {
        album = tableAlbums.getSelectionModel().getSelectedItem();
        ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
        AlbumActionDropDown albumActionDropDown = new AlbumActionDropDown();
        albumAction.getMenus().setAll(albumActionDropDown.setMenu(album, mainPane, 1, 0, ae));
        albumAction.setVisible(true);
    }

    @FXML
    void onArtistsBtnClick(ActionEvent event) {
        tableAlbums.setVisible(false);
        tableSongs.setVisible(false);
        artistsTable.setVisible(true);
        artistAction.setVisible(false);
        songAction.setVisible(false);
        albumAction.setVisible(false);
        labelSearch.setText("Artists");
        labelSearch.setVisible(true);

        val api = ServerAPI.getInstance();
        val artists = api.getArtistsByRegex(regex);

        ArrayList<Artist> artistArrayList = new ArrayList<>();

        for (int i = 0; i < artists.size(); i++){
            artistArrayList.add(
                    new Artist(
                            artists.get(i).getString("name"),
                            artists.get(i).getString("description"),
                            String.valueOf(artists.get(i).getInt("id")),
                            "n")
            );
        }

        Controller<Artist> controller = new Controller<>();
        artistsTable.getItems().clear();
        artistsTable = controller.initialize(artistArrayList, new String[]{"name", "description", "id", "genre"}, artistsTable, 0, mainPane);
        artistsTable.setVisible(true);
        artistsTable.refresh();

    }


    @SneakyThrows
    @FXML
    void onArtistClicked(MouseEvent event) {
        artist = artistsTable.getSelectionModel().getSelectedItem();
        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
        initScreensFunctions.initSettingsScreen("Artist", "/ArtistSectionScreen.fxml", mainPane, Integer.parseInt(artist.getId()), 0);
    }

    @SneakyThrows
    @FXML
    void onBackLabelClick(MouseEvent event) {
        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
        initScreensFunctions.initMainScreen(new ActionEvent(event.getSource(), event.getTarget()));
    }

    @FXML
    void onSearchBtnClick(ActionEvent event) {
        this.regex = searchField.getText();
        tableAlbums.setVisible(false);
        tableSongs.setVisible(false);
        artistsTable.setVisible(false);
        albumAction.setVisible(false);
        artistAction.setVisible(false);
        songAction.setVisible(false);
        labelSearch.setVisible(false);
    }

    @FXML
    void onSongItemClicked(MouseEvent event) {
        song = tableSongs.getSelectionModel().getSelectedItem();
        ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
        SongActionDropDown songActionDropDown = new SongActionDropDown();
        songAction.getMenus().setAll(songActionDropDown.setMenu(song, mainPane, 0, Integer.parseInt(song.getAlbumId()), ae));
        songAction.setVisible(true);
    }

    @FXML
    void onSongsBtnClick(ActionEvent event) {
        tableAlbums.setVisible(false);
        tableSongs.setVisible(true);
        artistsTable.setVisible(false);
        artistAction.setVisible(false);
        albumAction.setVisible(false);
        labelSearch.setText("Songs");
        labelSearch.setVisible(true);

        Utils utils = new Utils();
        tableSongs.setVisible(true);
        this.api = ServerAPI.getInstance();
        val songs = api.songsByRegex(regex);
        tableSongs = utils.generateTableSongs(tableSongs, songs, mainPane);
    }

    public void load(String regex){
        this.regex = regex;
        labelSearch.setVisible(false);
        tableAlbums.setVisible(false);
        tableSongs.setVisible(false);
        artistsTable.setVisible(false);
        albumAction.setVisible(false);
        artistAction.setVisible(false);
        songAction.setVisible(false);
    }

}
