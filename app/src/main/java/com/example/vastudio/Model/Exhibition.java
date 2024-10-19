package com.example.vastudio.Model;


public class Exhibition {
    private String id;
    private String name;
    private String date;
    private String enddate;
    private String location;
    private String artist;

    // Empty constructor for Firebase
    public Exhibition() {}

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Exhibition(String id, String name, String artist, String date, String endDate) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.enddate = endDate;
        this.artist = artist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Getters and setters
}
