package com.example.vastudio.Model;

public class Post {
    private String imageUrl;
    private String postId;
    private String publisherUid;
    private String imageFileName;
    private String artist;
    private String title;
    private String dsecription;
    private String price;
    private String bid;

    public Post(String imageUrl, String postId, String publisherUid, String imageFileName, String artist, String title, String dsecription, String price, String bid) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.publisherUid = publisherUid;
        this.imageFileName = imageFileName;
        this.artist = artist;
        this.title = title;
        this.dsecription = dsecription;
        this.price = price;
        this.bid = bid;
    }

    public Post(String imageUrl, String postId, String publisherUid, String artist, String title, String dsecription, String price) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.publisherUid = publisherUid;
        this.artist = artist;
        this.title = title;
        this.dsecription = dsecription;
        this.price = price;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDsecription() {
        return dsecription;
    }

    public void setDsecription(String dsecription) {
        this.dsecription = dsecription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Post() {}

    public Post(String imageUrl, String postId, String publisherUid, String imageFileName) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.publisherUid = publisherUid;
        this.imageFileName = imageFileName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisherUid() {
        return publisherUid;
    }

    public void setPublisherUid(String publisherUid) {
        this.publisherUid = publisherUid;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}