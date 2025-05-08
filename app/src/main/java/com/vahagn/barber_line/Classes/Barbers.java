package com.vahagn.barber_line.Classes;

import java.io.Serializable;
import java.util.List;

public class Barbers implements Serializable {
    private int barberId;
    private int barberShopsId;
    private String name;
    private String phoneNumber;
    private String image;
    private String workPlace;
    private double rating;
    private List<Services> services;
    private List<Reviews> reviews;
    private List<Appointment> appointments;
    private String email;
    private String joinType;
    private String status;

    public Barbers() {
    }

    public Barbers(int barberId, int barberShopsId) {
        this.barberId = barberId;
        this.barberShopsId = barberShopsId;

    }

    public Barbers(String image, String name,String phoneNumber, List<Services> services,int barberId, int barberShopsId) {
        this.image = image;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.services = services;
        this.barberId = barberId;
        this.barberShopsId = barberShopsId;
        this.rating = 5;
    }

    public Barbers(String image, String name, String phoneNumber, List<Services> services) {
        this.image = image;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.services = services;

//        this.rating = 5;
    }

    public Barbers(String image, String name, String phoneNumber, List<Services> services, String workPlace) {
        this.image = image;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.services = services;
        this.rating = 5;
        this.workPlace = workPlace;
    }
    public Barbers(String image, String name, String phoneNumber, List<Services> services, String workPlace, String email,String joinType) {
        this.image = image;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.services = services;
        this.rating = 5;
        this.workPlace = workPlace;
        this.email = email;
        this.joinType = joinType;
    }
    public Barbers(String name, String phoneNumber, String image, String workPlace, double rating, List<Services> services, List<Reviews> reviews, List<Appointment> appointments) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.workPlace = workPlace;
        this.rating = rating;
        this.services = services;
        this.reviews = reviews;
        this.appointments = appointments;
    }

    public int getBarberId() {
        return barberId;
    }

    public void setBarberId(int barberId) {
        this.barberId = barberId;
    }

    public int getBarberShopsId() {
        return barberShopsId;
    }

    public void setBarberShopsId(int barberShopsId) {
        this.barberShopsId = barberShopsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public List<Services> getServices() {
        return services;
    }

    public void setServices(List<Services> services) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}