package com.musicverse.client.collections.eventHandlers;

import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.collections.eventHandlers.EventHandler;
import com.musicverse.client.objects.Album;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;

@Deprecated
public class AlbumTableHandler implements EventHandler<Album> {
    @SneakyThrows
    @Override
    public void handler(Album album, AnchorPane mainPane) {

        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();

        initScreensFunctions.initSettingsScreen("Artist", "/ArtistSectionScreen.fxml",
                mainPane, Integer.parseInt(album.getArtist_id()), Integer.parseInt(album.getId()));

    }
}
