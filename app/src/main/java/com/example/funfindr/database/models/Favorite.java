package com.example.funfindr.database.models;


public class Favorite  {
    private String id;
    private String address;
    private String admin;
    private String subAdmin;
    private String locality;
    private String postalCode;
    private String countryName;

    // empty constructor
    public Favorite(){}

    public Favorite(String _id, String _address, String _admin, String _subAdmin, String _locality, String _postalCode, String _countryName)
    {
        this.id = _id;
        this.address = _address;
        this.admin = _admin;
        this.subAdmin = _subAdmin;
        this.locality = _locality;
        this.postalCode = _postalCode;
        this.countryName = _countryName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getSubAdmin() {
        return subAdmin;
    }

    public void setSubAdmin(String subAdmin) {
        this.subAdmin = subAdmin;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
