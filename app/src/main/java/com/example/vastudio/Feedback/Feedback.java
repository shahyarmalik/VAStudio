package com.example.vastudio.Feedback;
public class Feedback {
    private String id;
    private String type;
    private String details;
    private String timestamp;

    public Feedback(String id, String type, String details, String timestamp) {
        this.id = id;
        this.type = type;
        this.details = details;
        this.timestamp = timestamp;
    }

    public Feedback() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

