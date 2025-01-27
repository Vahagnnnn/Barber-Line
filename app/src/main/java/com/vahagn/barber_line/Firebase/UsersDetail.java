package com.vahagn.barber_line.Firebase;

import java.util.List;
import java.util.UUID;

public class UsersDetail{
    public String id;
    public String phoneNumber;
    public String name;
    public String passwordHash;
    public String email;
    public String profilePictureUrl;
    public List<Appointment> appointments;

    public UsersDetail(String phoneNumber, String name, String passwordHash, String email) {
        this.id = UUID.randomUUID().toString();
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}

class Appointment {
    public String barbershopName;
    public String barberName;
    public String serviceType;
    public String dateTime; // ISO-8601 format preferred (e.g., "2025-01-27T14:30:00")
    public double price;

    public Appointment(String barbershopName, String barberName, String serviceType, String dateTime, double price) {
        this.barbershopName = barbershopName;
        this.barberName = barberName;
        this.serviceType = serviceType;
        this.dateTime = dateTime;
        this.price = price;
    }
}
