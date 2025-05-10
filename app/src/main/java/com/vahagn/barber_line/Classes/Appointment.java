package com.vahagn.barber_line.Classes;

import java.io.Serializable;

public class Appointment implements Serializable {
    public String userEmail;
    public String userName;
    public String userPhoneNumber;
    public String userImageUrl;
    public String barbershopImageUrl;
    public String barbershopName;
    public String barbershopAddress;
    public String barbershopCoordinates;
    public String barbershopRating;
    public String barbershopOwnerEmail;
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
    public String uniqueID;
    private int barberId;
    private int barberShopsId;
    public Appointment() {
    }

    public Appointment(String userEmail,String userName,String userPhoneNumber,String userImageUrl, String barbershopImageUrl, String barbershopName,
                       String barbershopAddress,String barbershopCoordinates , String barbershopRating,String barbershopOwnerEmail,String weekDay_monthName_dayOfMonth,
                       String time, String barberImageUrl, String barberName, String barberRating,String serviceName,
                       String servicePrice,String serviceDuration,String status,String message_or_requests,String uniqueID,int barberId, int barberShopsId) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userImageUrl = userImageUrl;
        this.barbershopImageUrl = barbershopImageUrl;
        this.barbershopName = barbershopName;
        this.barbershopAddress = barbershopAddress;
        this.barbershopCoordinates = barbershopCoordinates;
        this.barbershopRating = barbershopRating;
        this.barbershopOwnerEmail = barbershopOwnerEmail;
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
        this.uniqueID = uniqueID;
        this.barberId = barberId;
        this.barberShopsId = barberShopsId;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
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

    public String getBarbershopCoordinates() {
        return barbershopCoordinates;
    }

    public void setBarbershopCoordinates(String barbershopCoordinates) {
        this.barbershopCoordinates = barbershopCoordinates;
    }

    public String getBarbershopRating() {
        return barbershopRating;
    }

    public void setBarbershopRating(String barbershopRating) {
        this.barbershopRating = barbershopRating;
    }

    public String getBarbershopOwnerEmail() {
        return barbershopOwnerEmail;
    }

    public void setBarbershopOwnerEmail(String barbershopOwnerEmail) {
        this.barbershopOwnerEmail = barbershopOwnerEmail;
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

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
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
}