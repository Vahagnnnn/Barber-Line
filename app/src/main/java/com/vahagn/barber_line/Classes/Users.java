package com.vahagn.barber_line.Classes;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Users {
    private String id;
    //    public String ID;
    public String first_name;
    public String last_name;
    public String name;
    public String email;
    public String password;
    public String phoneNumber;
    public String photoUrl;
    private String image;
    private int imageResId;
    public String passwordHash;
    public String profilePictureUrl;
    public List<Appointment> appointments;
    public List<Integer> Favourite_Barbershops = new ArrayList<>();

    public Users(String first_name, String last_name, String email, String password, String phoneNumber, String photoUrl,List<Integer> Favourite_Barbershops) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
        this.Favourite_Barbershops = Favourite_Barbershops;
    }
    public Users(String first_name, String last_name, String email, String password, String phoneNumber, String photoUrl) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
    }
//    public Users(String first_name, String last_name, String email, String password, String photoUrl) {
//        this.first_name = first_name;
//        this.last_name = last_name;
//        this.email = email;
//        this.password = password;
//        this.photoUrl = photoUrl;
//    }

    public Users(String name, String email, String password, String image, String phoneNumber, String passwordHash, String profilePictureUrl, List<Appointment> appointments) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.profilePictureUrl = profilePictureUrl;
        this.appointments = appointments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Integer> getFavourite_Barbershops() {
        return Favourite_Barbershops;
    }

    public void setFavourite_Barbershops(List<Integer> favourite_Barbershops) {
        Favourite_Barbershops = favourite_Barbershops;
    }
}