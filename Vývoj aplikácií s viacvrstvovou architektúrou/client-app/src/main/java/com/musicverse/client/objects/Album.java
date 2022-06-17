package com.musicverse.client.objects;

public class Album  {

    private String id, name, description, artist_id, genre_id, genre, artist;

    public Album(String id, String name, String description, String artist_id, String genre_id, String artist, String genre) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.artist_id = artist_id;
        this.genre_id = genre_id;
        this.genre = genre;
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public String getGenre_id() {
        return genre_id;
    }

    public String getGenre() {
        return genre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public void setGenre_id(String genre_id) {
        this.genre_id = genre_id;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String genre) {
        this.artist = artist;
    }
}
