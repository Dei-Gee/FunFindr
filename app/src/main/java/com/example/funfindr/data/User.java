package com.example.funfindr.data;

import android.graphics.Bitmap;

/**
 * Represents a User object
 */
public class User {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Bitmap profileImage;

    // public constructors
    public User() {}

    public User(String firstName, String lastName, String email, String password, Bitmap profilePic) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.password = password;
        this.profileImage = profilePic;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }


}
