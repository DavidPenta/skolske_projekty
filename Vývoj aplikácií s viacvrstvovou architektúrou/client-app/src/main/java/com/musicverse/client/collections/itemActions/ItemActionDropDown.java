package com.musicverse.client.collections.itemActions;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

/**
 * interface sluzi na interakciu s itemami v tableViews
 * @param <T> moze byt Album alebo Song, podla s akou TableView interagujeme
 */
public interface ItemActionDropDown<T> {

     /**
      * Menu setMenu nam vrati nastavene dropDown menu ktore je viditelne po kliknuti na item v tableView Album alebo Song
      * sluzi na interakciu s oznacenym itemom
      * @param item Album alebo Song objekt - oznaceny v listView
      * @param pane - AnchorPane screen-u z ktoreho metodu volame aby sme mohli prepnut Stage
      * @param id - na rozlisenie toho co ma byt dostupne v dropDown menu pre usera
      * @param selectedId - id oznacenej polozky v tableView
      * @param event - ActionEvent sluzi na to iste ako pane
      * @return navratova hodnota je nakonfigurovane Menu
      */
     Menu setMenu(T item, AnchorPane pane, int id, int selectedId, ActionEvent event);

}
