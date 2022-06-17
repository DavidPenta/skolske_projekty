package com.musicverse.client.player;

import com.musicverse.client.IOUtil;
import com.musicverse.client.api.ServerAPI;
import lombok.Getter;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MediaManager {
    public static final MediaManager instance = new MediaManager();

    @Getter
    private final AudioPlayer player = new AudioPlayer();
    private final Map<Integer, File> loadedSongs = new HashMap<>();

    public void setCurrentSong(int ID) throws MusicPlayerException {
        if (!loadedSongs.containsKey(ID)) {
            loadSong(ID);
        }
        val song = loadedSongs.get(ID);
        player.loadSong(song);
    }

    private void loadSong(int ID) throws MusicPlayerException {
        val songFile = IOUtil.getSongFile(ID);
        if (!songFile.exists()) {
            downloadSong(ID);
        }
        loadedSongs.put(ID, songFile);
    }

    private void downloadSong(int ID) throws MusicPlayerException {
        if (!ServerAPI.getInstance().downloadSongData(ID)) {
            throw new MusicPlayerException("Could not download song with ID " + ID + " from server!");
        }
    }
}
