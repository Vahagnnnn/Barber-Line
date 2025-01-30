package com.vahagn.barber_line.Classes;

import java.util.List;

public class Barbers {
    private int ID;
    private String name;
    private int phone;
    private String image;
    private int imageResId;
    private String workPlace;
    private double rating;
    private List<Service> services;
    private List<Reviews> reviews;
    private List<Appointment> appointments;

    public Barbers() {
    }

    public Barbers(int ID, String name, int phone, String image, int imageResId, String workPlace, double rating, List<Service> services, List<Reviews> reviews, List<Appointment> appointments) {
        this.ID = ID;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.imageResId = imageResId;
        this.workPlace = workPlace;
        this.rating = rating;
        this.services = services;
        this.reviews = reviews;
        this.appointments = appointments;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
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

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}