package com.musicverse.client.collections.eventHandlers;

import com.musicverse.client.InitScreensFunctions;
import com.musicverse.client.collections.eventHandlers.EventHandler;
import com.musicverse.client.objects.Artist;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;

@Deprecated
public class ArtistTableHandler implements EventHandler<Artist> {
    @SneakyThrows
    @Override
    public void handler(Artist artist, AnchorPane mainPane) {
        System.out.println(artist.getId());
        InitScreensFunctions initScreensFunctions = new InitScreensFunctions();
        initScreensFunctions.initSettingsScreen(
                "Artist",
                "/ArtistSectionScreen.fxml",
                mainPane,
                Integer.parseInt(artist.getId()),
                0
        );

    }
}
