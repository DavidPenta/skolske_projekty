package com.musicverse.client.objects;

public class Song {

    private String albumId;
    private String data;
    private String description;
    private String duration;
    private String id;
    private String name;
    private String artistId;
    private String genre_id;
    private String genre;
    private String album;
    private String artist;

    public Song(String albumId, String data, String description, String name, String album, String artist,
                String duration, String genre, String id, String artistId, String genre_id) {
        this.albumId = albumId;
        this.data = data;
        this.description = description;
        this.name = name;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.genre = genre;
        this.id = id;
        this.artistId = artistId;
        this.genre_id = genre_id;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(String genre_id) {
        this.genre_id = genre_id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getData() {
        return data;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

}
