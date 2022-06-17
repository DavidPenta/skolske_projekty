package com.musicverse.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.falsepattern.json.node.JsonNode;
import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.collections.Controller;
import com.musicverse.client.collections.Utils;
import com.musicverse.client.collections.itemActions.SongActionDropDown;
import com.musicverse.client.objects.Artist;
import com.musicverse.client.objects.Playlist;
import com.musicverse.client.objects.Song;
import com.musicverse.client.sessionManagement.MyLogger;
import com.musicverse.client.sessionManagement.PreferencesLogin;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import com.musicverse.client.api.ServerAPI;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import lombok.val;
import com.musicverse.client.Localozator;

public class MainScreen {


    @FXML
    public AnchorPane mainPane;

    @FXML
    private Label likedAlbumsLabel1;

    @FXML
    private Label likedArtistsLabel;

    @FXML
    private Label likedSongsLabel1;

    @FXML
    private Label loginMainScreenLabel;

    @FXML
    private Label nickLabel;

    @FXML
    private Label playlistsLabel;

    @FXML
    private ListView listOfPlaylists;


    @FXML
    private Label registerMainScreenLabel;


    @FXML
    private MenuItem settingsMenuItemSettings;

    @FXML
    private MenuButton settingsDropDown;

    @FXML
    private MenuBar menuBarTest;

    @FXML
    private MenuItem settingsMenuItemAA;

    private int role = -1;

    @FXML
    private Label homeLabel;

    private String userName = Localozator.getResourceBundle().getString("GUEST");

    private int genre_id = -1;

    private ServerAPI server_api;

    @FXML
    private GridPane rectanglesGrid;

    @FXML
    private TableView<Song> tableSongs;

    private int showType = 0;

    @FXML
    private TableView<Artist> tableView;

    public MainScreen(){
        try {
            this.server_api = ServerAPI.getInstance();
            this.showType = 0;
        }
        catch (Exception e){
            new MyLogger(e.toString(),"ERROR");
            e.printStackTrace();
        }

    }

    static String randomColor(){

        int red = (int)Math.floor(Math.random()*(255-1+1)+1);
        int green = (int)Math.floor(Math.random()*(255-1+1)+1);
        int blue = (int)Math.floor(Math.random()*(255-1+1)+1);

        String red1 = String.valueOf(red);
        String green1 = String.valueOf(green);
        String blue1 = String.valueOf(blue);

        return "-fx-background-color: rgba(" + red1 +"," + green1 + "," + blue1 + ",0.99); -fx-pref-height: 60px";
    }

    public void setRectangles(ArrayList<String> items){

        btnDeletePlaylist.setVisible(false);
        playlistDescriptionBtn.setVisible(false);

        if (PreferencesLogin.getPrefs().getId() < 1)
            newPlaylistBtn.setVisible(false);

        int rowNum = (int) Math.ceil((double) items.size() / 6);

        for (int i = 0; i < rowNum; i++) {
            RowConstraints rowConst = new RowConstraints(100);
            rowConst.setPercentHeight(100.0 / rowNum);
            rectanglesGrid.getRowConstraints().add(rowConst);
        }


        int counter = 0;
        for (int row=0; row < rowNum; row++) {
            for (int col=0; col < 6; col++){

                Label labelOfBox = new Label(items.get(counter));
                labelOfBox.setStyle("-fx-text-fill: white; -fx-font-size: 25px; -fx-padding: 5; -fx-font-weight: bold");

                Pane pane = new Pane();
                pane.setStyle(randomColor());

                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().add(labelOfBox);

                pane.setOnMouseReleased(e -> {

                    this.genre_id = setGenreId(labelOfBox.getText(),items);
                    val artists = this.server_api.getArtistsByGenre(genre_id);

                    ArrayList<Artist> artistArrayList = new ArrayList<>();

                    for (int i = 0; i < artists.size(); i++){
                        artistArrayList.add(
                                new Artist(
                                        artists.get(i).getString("name"),
                                        artists.get(i).getString("description"),
                                        String.valueOf(artists.get(i).getInt("id")),
                                        labelOfBox.getText())
                                );
                    }

                    Controller<Artist> controller = new Controller<>();
                    tableView.getItems().clear();
                    tableView = controller.initialize(artistArrayList, new String[]{"name", "description", "id", "genre"}, tableView, 0, mainPane);
                    tableView.setVisible(true);
                    tableView.refresh();




                   //TEST API
                   //val songs1 = this.server_api.songsByAlbum(1);
                   //val songs2 = this.server_api.songsByPlaylist(1);

                });

                pane.getChildren().add(anchorPane);
                rectanglesGrid.add(pane, col, row);

                if (counter == (items.size() -1)){
                    break;
                }
                counter++;
            }
            if (counter == (items.size() )){
                break;
            }
        }

    }

    @FXML
    private MenuBar songItemAction;

    private ActionEvent e;

    @FXML

    private Song selectedSong = null;

    private int selectedPlaylistId = 0;


    @FXML
    private Button newPlaylistBtn;


    @FXML
    private TextField searchField;

    @SneakyThrows
    @FXML
    void onSearchButtonClick(ActionEvent event) {
        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
        initScreensFunctions.initSearchScreen(searchField.getText(), event);
    }


    @FXML
    void onBtnNewPlaylistClick(ActionEvent event2) {

        val api = ServerAPI.getInstance();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialog.setHeight(500);

        TextField nameField = new TextField(Localozator.getResourceBundle().getString("PLAYLISTNAME"));
        TextField descriptionField = new TextField(Localozator.getResourceBundle().getString("DESCRIPTION"));
        Button buttonSave = new Button(Localozator.getResourceBundle().getString("SAVE"));
        Button buttonCancel = new Button(Localozator.getResourceBundle().getString("CANCEL"));

        dialogVbox.getChildren().add(new Text(Localozator.getResourceBundle().getString("NAME OF PLAYLIST")));
        dialogVbox.getChildren().add(nameField);

        dialogVbox.getChildren().add(new Text(Localozator.getResourceBundle().getString("ADD DESCRIPTION")));
        dialogVbox.getChildren().add(descriptionField);

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

                if (api.createCollection(PreferencesLogin.getPrefs().getId(),
                        nameField.getText(), descriptionField.getText(), 0)){

                    InitScreensFunctions initScreensFunctions = new InitScreensFunctions();

                    try {
                        dialog.close();
                        initScreensFunctions.initMainScreen(event2);

                    } catch (IOException ex) {
                        new MyLogger(ex.toString(),"ERROR");
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

    }

    @FXML
    public void onSongItemClicked(MouseEvent event) {
        ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
        SongActionDropDown songActionDropDown = new SongActionDropDown();
        selectedSong = tableSongs.getSelectionModel().getSelectedItem();
        songItemAction.setVisible(true);
        songItemAction.getMenus().add(songActionDropDown.setMenu(selectedSong, mainPane, 0, selectedPlaylistId,  ae));

    }


    @FXML
    private Button btnDeletePlaylist;
    @FXML
    void onBtnDeletePlaylistClick(ActionEvent event) throws IOException {
        if(server_api.deleteCollection(selectedPlaylistId, 0)){
            InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
            initScreensFunctions.initMainScreen(event);
        }
    }


    public void setRole(int i){

        this.role = i;

        btnDeletePlaylist.setVisible(false);
        SettingsDropDown settingsDropDownImported = new SettingsDropDown();
        menuBarTest.getMenus().add(settingsDropDownImported.menu(role, mainPane, "main"));

        registerMainScreenLabel.setVisible(false);
        loginMainScreenLabel.setVisible(false);
        nickLabel.setText(userName);
        System.out.println(role);
        if (role > 0)
            nickLabel.setText(PreferencesLogin.getPrefs().getNickName());

        switch (role) {
            case -1:
                // here comes code, when user is not verified in case he is atempting to login and he is not saved user
                break;

            case 0:
                playlistsLabel.setText(Localozator.getResourceBundle().getString("ONLY FOR REGISTERED"));
                //likedAlbumsLabel1.setVisible(false);
                //likedArtistsLabel.setVisible(false);
                //likedSongsLabel1.setVisible(false);
                registerMainScreenLabel.setVisible(true);
                loginMainScreenLabel.setVisible(true);
                break;

            case 1:
                break;

            case 2:
                break;
            case 3:
            default:
                break;
        }
    }

    private String playlistDescription;

    public void setPlaylists(HashMap<Integer,String> list, ArrayList<Playlist> playlistArrayList){

        for (String name : list.values()){
            listOfPlaylists.getItems().add(name);
        }

        listOfPlaylists.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btnDeletePlaylist.setVisible(true);
                playlistDescriptionBtn.setVisible(true);
                int result = getKeyByValue(list, listOfPlaylists.getSelectionModel().getSelectedItem());

                for (Playlist playlist : playlistArrayList) {
                    if (playlist.getId() == result)
                        playlistDescription = playlist.getDescription();
                }

                val songs2 = server_api.songsByPlaylist(result);
                selectedPlaylistId = result;

                Utils utils = new Utils();
                tableSongs = utils.generateTableSongs(tableSongs, songs2, mainPane);

            }
        });

    }

    public static <T,E> T getKeyByValue(HashMap<T, E> map, Object value) {
        for (Map.Entry<T,E> entry : map.entrySet()){
            if (Objects.equals(value, entry.getValue())){
                return entry.getKey();
            }
        }
        return (T) Integer.valueOf(0);
    }

    public int setGenreId(String name,ArrayList<String> list){
        for (int i=0;i<list.size();i++){
            if(list.get(i).equals(name))
               return i+1;
        }
        return -1;
    }


    public int getGenreId(){
        return this.genre_id;
    }


    public ArrayList<String> getArtistsNames(){
        val artists = this.server_api.getArtistsByGenre(getGenreId());
        ArrayList<String> filtered = new ArrayList<>();
        for (int i = 0; i < artists.size(); i++)
            filtered.add(artists.get(i).getString("name"));
        return filtered;
    }




    @FXML
    void loginLabelClick(MouseEvent event) throws IOException {
        ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
        initScreensFunctions.initLoginScreen(ae);
    }

    @FXML
    void registerLabelClick(MouseEvent event) throws IOException {
        ActionEvent ae = new ActionEvent(event.getSource(), event.getTarget());
        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
        initScreensFunctions.initRegistrationScreen(ae, 1);
    }

    @FXML
    private Button playlistDescriptionBtn;
    @FXML
    void onPlaylistDescriptionBtnClick(ActionEvent event) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialog.setHeight(400);

        Button buttonCancel = new Button(Localozator.getResourceBundle().getString("CANCEL"));

        dialogVbox.getChildren().add(new Text(Localozator.getResourceBundle().getString("PLAYLIST DESCRIPTION")));

        dialogVbox.getChildren().add(new Text(playlistDescription));

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
    }


    @FXML
    void onHomeClicked(MouseEvent event) {
        tableView.setVisible(false);
        tableSongs.setVisible(false);
        songItemAction.setVisible(false);
        btnDeletePlaylist.setVisible(false);
        playlistDescriptionBtn.setVisible(false);
    }

    @SneakyThrows
    @FXML
    void onArtistsTableClicked(MouseEvent event) {
        Artist artist = tableView.getSelectionModel().getSelectedItem();
        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
        try {
            initScreensFunctions.initSettingsScreen("Artist", "/ArtistSectionScreen.fxml", mainPane, Integer.parseInt(artist.getId()), 0);
        }
        catch (Exception e){
            new MyLogger(e.toString(),"ERROR");
        }

    }

}
