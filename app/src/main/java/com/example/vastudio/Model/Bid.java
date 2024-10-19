package com.example.vastudio.Model;
public class Bid {
    private String bidId;
    private String bidderId;
    private String postId;
    private String title;
    private String publisherid;
    private String url;
    private String paymentStatus;
    private double bidAmount;

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public Bid(String bidId, String bidderId, String postId, String title, String publisherid, String url, String paymentStatus, double bidAmount) {
        this.bidId = bidId;
        this.bidderId = bidderId;
        this.postId = postId;
        this.title = title;
        this.publisherid = publisherid;
        this.url = url;
        this.paymentStatus = paymentStatus;
        this.bidAmount = bidAmount;
    }

    public Bid(String bidderId, String postId, String title, String publisherid, String url, String paymentStatus, double bidAmount) {
        this.bidderId = bidderId;
        this.postId = postId;
        this.title = title;
        this.publisherid = publisherid;
        this.url = url;
        this.paymentStatus = paymentStatus;
        this.bidAmount = bidAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPublisherid() {
        return publisherid;
    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setPublisherid(String publisherid) {
        this.publisherid = publisherid;
    }

    public Bid() {
        // Default constructor required for Firebase
    }

    public Bid(String bidderId, String postId, String title, String publisherid, String url, double bidAmount) {
        this.bidderId = bidderId;
        this.postId = postId;
        this.title = title;
        this.publisherid = publisherid;
        this.url = url;
        this.bidAmount = bidAmount;
    }

    public Bid(String bidderId, String title, String artworkId, double bidAmount) {
        this.bidderId = bidderId;
        this.title = title;
        this.postId = artworkId;
        this.bidAmount = bidAmount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBidderId() {
        return bidderId;
    }

    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }

    public String getArtworkId() {
        return postId;
    }

    public void setArtworkId(String artworkId) {
        this.postId = artworkId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }

    // Getters and setters for attributes
}

