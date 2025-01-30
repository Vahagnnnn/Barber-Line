package com.vahagn.barber_line.Classes;

import java.io.Serializable;

public class Reviews implements Serializable {
    private String userName;
    private String reviewText;
    private double rating;
    private String date;

    public Reviews() {}

    public Reviews(String userName, String reviewText, double rating, String date) {
        this.userName = userName;
        this.reviewText = reviewText;
        this.rating = rating;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
