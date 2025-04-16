package com.vahagn.barber_line.Classes;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class BarberShops {
    private String ownerEmail;
    private String name;
    private String address;
    private String coordinates;
    private String image;
    private String logo;
    private String status;
    private double rating;
    private List<Reviews> reviews;
    private List<Services> services;
    private List<Barbers> specialists;
    private Map<String, TimeRange> openingTimes;
    private String reason;

    public BarberShops() {
    }

//    public BarberShops(String ownerEmail,String name, String address, String coordinates,String image, String logo,String status,
//                       List<Services> services, List<Barbers> specialists) {
//        this.ownerEmail = ownerEmail;
//        this.name = name;
//        this.address = address;
//        this.coordinates = coordinates;
//        this.image = image;
//        this.status = status;
//        this.logo = logo;
//        this.rating = 5;
//        this.services = services;
//        this.specialists = specialists;
//    }

    public BarberShops(String ownerEmail, String name, String address, String coordinates, String image, String logo, String status,
                       List<Barbers> specialists,Map<String, TimeRange> openingTimes) {
        this.ownerEmail = ownerEmail;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.image = image;
        this.logo = logo;
        this.rating = 5;
        this.status = status;
        this.specialists = specialists;
        this.openingTimes = openingTimes;
    }

    public BarberShops(String ownerEmail, String name, String address, String coordinates, String image, String logo, double rating,
                       List<Reviews> reviews, List<Services> services, List<Barbers> specialists, Map<String, TimeRange> openingTimes) {
        this.ownerEmail = ownerEmail;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.image = image;
        this.logo = logo;
        this.rating = rating;
        this.reviews = reviews;
        this.services = services;
        this.specialists = specialists;
        this.openingTimes = openingTimes;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
        this.services = services;
    }

    public List<Barbers> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(List<Barbers> specialists) {
        this.specialists = specialists;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Map<String, TimeRange> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(Map<String, TimeRange> openingTimes) {
        this.openingTimes = openingTimes;
    }
}
