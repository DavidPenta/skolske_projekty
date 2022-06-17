package com.musicverse.client.objects;

public class Artist {
    private String name;
    private String description;
    private String id;
    private String genre;

    public Artist(String name, String description, String id, String genre) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
