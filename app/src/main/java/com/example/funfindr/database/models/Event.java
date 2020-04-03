package com.example.funfindr.database.models;

public class Event {
    private String id = "";
    private String title = "";
    private String location = "";
    private String address = "";
    private String type = "";
    private String notes = "";
    private String date = null;

    // Empty Constructor
    public Event(){}

    // Public Constructor
    public Event(String _id, String titl, String loc, String addr, String typ, String notess, String datee) {
        this.address = addr;
        this.date = datee;
        this.location = loc;
        this.notes = notess;
        this.type = typ;
        this.title = titl;
        this.id = _id;
    }

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
