package com.vahagn.barber_line.Classes;

class Appointment {
    public String userName;
    public String barbershopName;
    public String barberName;
    public String serviceType;
    public String dateTime; // ISO-8601 format preferred (e.g., "2025-01-27T14:30:00")
    public double price;

    public Appointment() {
    }

    public Appointment(String userName, String barbershopName, String barberName, String serviceType, String dateTime, double price) {
        this.userName = userName;
        this.barbershopName = barbershopName;
        this.barberName = barberName;
        this.serviceType = serviceType;
        this.dateTime = dateTime;
        this.price = price;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBarbershopName() {
        return barbershopName;
    }

    public void setBarbershopName(String barbershopName) {
        this.barbershopName = barbershopName;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}