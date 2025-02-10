package com.vahagn.barber_line.Classes;

import android.net.Uri;

import java.util.List;
import java.util.UUID;

public class Users {
    public String ID;
    public String Firstname_LastnameText;
    public String name;
    public String email;
    public String password;
    private String image;
    private int imageResId;
    private Uri photoUrl;
    public String phoneNumber;
    public String passwordHash;
    public String profilePictureUrl;
    public List<Appointment> appointments;

    public Users(String Firstname_LastnameText, String email) {
        this.Firstname_LastnameText = Firstname_LastnameText;
        this.email = email;
    }

    public Users(String Firstname_LastnameText, String email, Uri photoUrl) {
        this.Firstname_LastnameText = Firstname_LastnameText;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public Users(String name, String email, String password, String image, String phoneNumber, String passwordHash, String profilePictureUrl, List<Appointment> appointments) {
        this.ID = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.profilePictureUrl = profilePictureUrl;
        this.appointments = appointments;
    }
}