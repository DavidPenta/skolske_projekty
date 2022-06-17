package com.musicverse.client;

import com.musicverse.client.collections.Utils;
import com.musicverse.client.objects.Artist;
import com.musicverse.client.objects.Playlist;
import com.musicverse.client.objects.Song;
import com.musicverse.client.sessionManagement.MyLogger;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import com.musicverse.client.api.ServerAPI;
import lombok.val;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * metody v tejto triede sluzia na prepinanie medzi obrazovkami.
 * Pomocou FXMLLoader ziskame fxml subor so obrazovkou
 * Preberieme Stage od aktualneho vlastnika pomocou ActionEvents alebo AnchorPane
 * a do noveho Stage nahrame novy screen
 */
public class InitScreensFunctions {


    /**
     *inicializacia hlavnej obrazovky
     */
    public void initMainScreen(ActionEvent event) throws IOException{
        ResourceBundle resources = Localozator.getResourceBundle();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/MainScreen.fxml")), resources);
        Parent root = loader.load();
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200,600);
        com.musicverse.client.gui.MainScreen controler = loader.getController();
        controler.setRole(PreferencesLogin.getPrefs().getRole());
        //String[] items = {"Rock", "Pop", "Metal", "Classical", "Chill", "Christmas", "Workout","Rock", "Pop", "Metal", "Classical", "Chill", "Christmas", "Workout","Rock", "Pop", "Metal", "Classical", "Chill", "Christmas", "Workout"};
        try {
            val api = ServerAPI.getInstance();
            val playlists = api.getPublicPlaylists();
            /*for (int i = 0; i<playlists.size();i++) {
                list.put(playlists.get(i).getInt("id"),playlists.get(i).getString("name"));
            }*/
            val genres = api.getGenres();

            ArrayList<String> genresList = new ArrayList<String>();
            for (int i = 0; i < genres.size(); i++)
                genresList.add(genres.get(i).getString("genre"));

            controler.setRectangles(genresList);

            val response = api.getUserPlaylists(PreferencesLogin.getPrefs().getId());
            ArrayList<Playlist> playlistArrayList = new ArrayList<>();
            for (int i = 0; i < response.size(); i++){
                playlistArrayList.add(new Playlist(
                        response.get(i).getString("name"),
                        response.get(i).getString("description"),
                        response.get(i).getInt("id")
                ));
            }
            controler.setPlaylists(Utils.createPlaylist(response), playlistArrayList);

        }
        catch (Exception e){
            new MyLogger(e.toString(),"ERROR");
            e.printStackTrace();
        }

        window.setTitle("MusicVerse");
        window.setScene(scene);
        window.show();
    }

    /**
     *inicializacia obrazovky registracie
     */
    @SneakyThrows
    public void initRegistrationScreen(ActionEvent actionEvent, int role) throws IOException{
        ResourceBundle resources = Localozator.getResourceBundle();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/RegisterScreen.fxml")), resources);
        Parent root = loader.load();
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        com.musicverse.client.gui.RegisterScreen controler = loader.getController();
        String label = "", Title = "";
        if (role == 1){
            label = Localozator.getResourceBundle().getString("REGISTER");
            Title = Localozator.getResourceBundle().getString("REGISTER NEW USER");
        } else if (role == 2) {
            label = Localozator.getResourceBundle().getString("ARTIST REQUEST");
            Title = Localozator.getResourceBundle().getString("REGISTER NEW ARTIST");
        }
        controler.setLabelText(label, role);
        Scene scene = new Scene(root, 1200,600);
        window.setTitle(Title);
        window.setScene(scene);
        window.show();
    }

    /**
     * inicializacia obrazovky login
     * @param actionEvent
     * @throws IOException
     */
    @SneakyThrows
    public void initLoginScreen(ActionEvent actionEvent) throws IOException{
        ResourceBundle resources = Localozator.getResourceBundle();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/LoginScreen.fxml")), resources);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200,600);
        window.setTitle("Log in user");
        window.setScene(scene);
        window.show();
    }

    @SneakyThrows
    public void initLoginScreen(AnchorPane actionEvent) throws IOException{
        ResourceBundle resources = Localozator.getResourceBundle();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/LoginScreen.fxml")), resources);
        Stage window = (Stage) ((Node) actionEvent).getScene().getWindow();
        Scene scene = new Scene(root, 1200,600);
        window.setTitle("Log in user");
        window.setScene(scene);
        window.show();
    }

    /**
     * inicializacia player screen
     * @param song
     * @throws IOException
     */
    @SneakyThrows
    public void initPlayerSscreen(Song song) throws IOException{
        ResourceBundle resources = Localozator.getResourceBundle();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/MediaPlayerScreen.fxml")), resources);
        Parent root = loader.load();
        Stage window = new Stage();
        com.musicverse.client.gui.MediaPlayerScreen controler = loader.getController();
        controler.load(song);
        Scene scene = new Scene(root, 600,190);
        window.setTitle("MusicVerse player");
        window.setScene(scene);
        window.show();
    }

    /**
     * search screen pre vyhladavanie pomocou zadaneho regex
     * @param regex
     * @param actionEvent
     * @throws IOException
     */
    @SneakyThrows
    public void initSearchScreen(String regex, ActionEvent actionEvent) throws IOException{
        ResourceBundle resources = Localozator.getResourceBundle();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/SearchScreen.fxml")), resources);
        Parent root = loader.load();
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        com.musicverse.client.gui.SearchScreen controler = loader.getController();
        controler.load(regex);
        Scene scene = new Scene(root, 1200,600);
        window.setTitle("MusicVerse searcher");
        window.setScene(scene);
        window.show();
    }

    /**
     * tu inicializujeme artist section, admin section a settings section
     * @param Title
     * @param fxml
     * @param pane
     * @param from
     * @param shownTable
     * @throws IOException
     */
    @SneakyThrows
    public void initSettingsScreen(String Title, String fxml, AnchorPane pane, int from, int shownTable) throws IOException{
        ResourceBundle resources = Localozator.getResourceBundle();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxml)), resources);
        Parent root = loader.load();
        Stage window = (Stage) pane.getScene().getWindow();
        if (Objects.equals(Title, "Artist")){
            com.musicverse.client.gui.ArtistSectionScreen controler = loader.getController();
            //controler.setSettings(from);
            controler.load(from, shownTable);
        }else if (Objects.equals(Title, "Settings")){
            com.musicverse.client.gui.SettingsScreen controler = loader.getController();
           // controler.setFrom(from);
        }else if (Objects.equals(Title, "Admin")){
            com.musicverse.client.gui.AdminSectionScreen controler = loader.getController();
            controler.setSettings("");
        }
        Scene scene = new Scene(root, 1200,600);
        window.setScene(scene);
        window.setTitle(Title);
        window.show();
    }


}
