package com.musicverse.client.collections;

import com.musicverse.client.collections.eventHandlers.AlbumTableHandler;
import com.musicverse.client.collections.eventHandlers.ArtistTableHandler;
import com.musicverse.client.collections.eventHandlers.EventHandler;
import com.musicverse.client.collections.eventHandlers.SongTableHandleer;
import com.musicverse.client.objects.Album;
import com.musicverse.client.objects.Artist;
import com.musicverse.client.objects.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Objects;

/**
 * genericka metoda Controller sluzi na vygenerovanie TableView Albums alebo Songs alebo Artists alebo Users alebo Requests.
 * Podla generika U.
 * @param <U> zadany objekt z package Objects
 */
public class Controller <U> {

    public Controller(){
    }

    /**
     * metoda initialize nam vygeneruje TableView na zaklade uvedeneho generika a dat ktore udavame v arrayListe
     * @param records predstavuju polozky typu U, ktore budu v tableView
     * @param columns stlpce ktore bude tableView obsahovat, musia sa zhodovat s argumentami konstruktora U
     * @param tableView tableView preddefinujeme v danej triede a potom ju vlozime ako argument a vratime tu istu instanciu
     *                  tableView ale naplnenu itemami
     * @param what rozlisovacia pomocka
     * @param mainPane vlastnik Stage-u
     * @return navratova hodnota je naplnena TableView
     */
    public TableView<U> initialize(ArrayList<U> records, String[] columns, TableView<U> tableView, int what, AnchorPane mainPane){

       ObservableList<U> data = FXCollections.observableArrayList(records);
        tableView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/tableViewCss.css")).toExternalForm());
        U u;
        tableView.getColumns().clear();

       ArrayList<TableColumn<U,String>> tableColumnArrayList = new ArrayList<>();
        for (String column : columns) {
            TableColumn<U, String> tableColumn = new TableColumn<>(column);
            tableColumn.setCellValueFactory(new PropertyValueFactory<U, String>(column));
            tableColumnArrayList.add(tableColumn);


            //specify column widths... sum must be 940 to take full width of table ARTISTS
            if (what == 0)
                switch (column) {
                    case "id":
                        tableColumn.setVisible(false);
                        break;
                    case "genre":
                        tableColumn.setPrefWidth(100);
                        break;
                    case "description":
                        tableColumn.setPrefWidth(600);
                        break;
                    case "name":
                        tableColumn.setPrefWidth(240);
                        break;
                }

            //SONGS
            if (what == 1)
                switch (column) {
                    case "id":
                    case "description":
                    case "data":
                    case "album_id":
                    case "artist_id":
                    case "genre_id":
                        tableColumn.setVisible(false);
                        break;

                    case "name":
                    case "album":
                        tableColumn.setPrefWidth(250);
                        break;

                    case "artist":
                        tableColumn.setPrefWidth(240);
                        break;

                    case "duration":

                    case "genre":
                        tableColumn.setPrefWidth(100);
                        break;
                }

            //id, name, description, artist_id, genre_id, genre, artist;
            //Album WIDTH 840
            if (what == 2)
                switch (column) {
                    case "id":
                    case "artist_id":
                    case "genre_id":
                        tableColumn.setVisible(false);
                        break;

                    case "name":
                        tableColumn.setPrefWidth(220);
                        break;

                    case "artist":
                        tableColumn.setPrefWidth(210);
                        break;

                    case "description":
                        tableColumn.setPrefWidth(310);
                        break;

                    case "genre":
                        tableColumn.setPrefWidth(100);
                        break;
                }

            if (what == 3) //table of users in admin section
                switch (column) {
                    case "id":
                    case "access_level":
                        tableColumn.setVisible(false);
                        break;

                    case "nick_name":
                    case "email":
                        tableColumn.setPrefWidth(176);
                        break;

                }

            if (what == 4) //table of requests in admin section
                switch (column) {
                    case "id":
                        tableColumn.setPrefWidth(39);
                        break;

                    case "name":
                        tableColumn.setPrefWidth(300);
                        break;

                }
        }
        if (data != null)
            tableView.setItems(data);
        boolean x = tableView.getColumns().addAll(tableColumnArrayList);

       /* tableView.setRowFactory(tv -> {
            TableRow<U> row = new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if (!row.isEmpty() && mouseEvent.getButton()== MouseButton.PRIMARY
                && mouseEvent.getClickCount() == 1) {
                    U clickedRow = row.getItem();
                    /*if (what == 0){
                        Artist artist = (Artist) clickedRow;
                        EventHandler<Artist> eventHandler = new ArtistTableHandler();
                        eventHandler.handler(artist, mainPane);
                    }
                    if (what == 1){
                        Song song = (Song) clickedRow;
                        EventHandler<Song> eventHandler = new SongTableHandleer();
                        eventHandler.handler(song, mainPane);
                    }
                    if (what == 2){
                        Album album = (Album) clickedRow;
                        EventHandler<Album> eventHandler = new AlbumTableHandler();
                        eventHandler.handler(album, mainPane);
                    }*/
                //}
            //});
            //return row;
        //});
        //System.out.println(x);/*

        return tableView;
    }


}
