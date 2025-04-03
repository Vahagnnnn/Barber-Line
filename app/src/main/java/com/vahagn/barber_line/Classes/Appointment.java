package com.vahagn.barber_line.Classes;

import java.io.Serializable;

public class Appointment implements Serializable {
    public String userName;
    public String barbershopImageUrl;
    public String barbershopName;
    public String barbershopAddress;
    public String weekDay_monthName_dayOfMonth;
    public String time;
    public String barberImageUrl;
    public String barberName;
    public String barberRating;
    public String serviceName;
    public String servicePrice;
    public String serviceDuration;
    public String status;
    public String message_or_requests;

    public Appointment() {
    }

    public Appointment(String userName, String barbershopImageUrl, String barbershopName, String barbershopAddress,String weekDay_monthName_dayOfMonth, String time, String barberImageUrl, String barberName, String barberRating,String serviceName,  String servicePrice,String serviceDuration,String status) {
        this.userName = userName;
        this.barbershopImageUrl = barbershopImageUrl;
        this.barbershopName = barbershopName;
        this.barbershopAddress = barbershopAddress;
        this.weekDay_monthName_dayOfMonth = weekDay_monthName_dayOfMonth;
        this.time = time;
        this.barberImageUrl = barberImageUrl;
        this.barberName = barberName;
        this.barberRating = barberRating;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDuration = serviceDuration;
        this.status = status;
    }

    public Appointment(String userName, String barbershopImageUrl, String barbershopName, String barbershopAddress,String weekDay_monthName_dayOfMonth, String time, String barberImageUrl, String barberName, String barberRating,String serviceName,  String servicePrice,String serviceDuration,String status,String message_or_requests) {
        this.userName = userName;
        this.barbershopImageUrl = barbershopImageUrl;
        this.barbershopName = barbershopName;
        this.barbershopAddress = barbershopAddress;
        this.weekDay_monthName_dayOfMonth = weekDay_monthName_dayOfMonth;
        this.time = time;
        this.barberImageUrl = barberImageUrl;
        this.barberName = barberName;
        this.barberRating = barberRating;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDuration = serviceDuration;
        this.status = status;
        this.message_or_requests = message_or_requests;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBarbershopImageUrl() {
        return barbershopImageUrl;
    }

    public void setBarbershopImageUrl(String barbershopImageUrl) {
        this.barbershopImageUrl = barbershopImageUrl;
    }

    public String getBarbershopName() {
        return barbershopName;
    }

    public void setBarbershopName(String barbershopName) {
        this.barbershopName = barbershopName;
    }

    public String getBarbershopAddress() {
        return barbershopAddress;
    }

    public void setBarbershopAddress(String barbershopAddress) {
        this.barbershopAddress = barbershopAddress;
    }


    public String getWeekDay_monthName_dayOfMonth() {
        return weekDay_monthName_dayOfMonth;
    }

    public void setWeekDay_monthName_dayOfMonth(String weekDay_monthName_dayOfMonth) {
        this.weekDay_monthName_dayOfMonth = weekDay_monthName_dayOfMonth;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBarberImageUrl() {
        return barberImageUrl;
    }

    public void setBarberImageUrl(String barberImageUrl) {
        this.barberImageUrl = barberImageUrl;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getBarberRating() {
        return barberRating;
    }

    public void setBarberRating(String barberRating) {
        this.barberRating = barberRating;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage_or_requests() {
        return message_or_requests;
    }

    public void setMessage_or_requests(String message_or_requests) {
        this.message_or_requests = message_or_requests;
    }
}