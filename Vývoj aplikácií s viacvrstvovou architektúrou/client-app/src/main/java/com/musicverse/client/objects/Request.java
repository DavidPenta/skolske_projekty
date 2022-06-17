package com.musicverse.client.objects;

public class Request {

    private int id;
    private String artistName;


    public Request(int id, String artistName) {
        this.id = id;
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
