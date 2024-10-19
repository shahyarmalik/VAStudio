package com.example.vastudio.Model;

public class User {
    private String Name;
    private String Email;
    private String Username;
    private String Id;
    private String ImageUrl;

    public User() {
    }

    public User(String name, String email, String username, String id, String imageUrl) {
        Name = name;
        Email = email;
        Username = username;
        Id = id;
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
