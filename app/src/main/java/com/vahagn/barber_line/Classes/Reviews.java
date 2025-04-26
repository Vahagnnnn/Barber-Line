package com.vahagn.barber_line.Classes;

import android.text.Editable;

import java.io.Serializable;

public class Reviews implements Serializable {
    private String CustomerImage;
    private String CustomerName;
    private String ReviewText;
    private double Rating;

    public Reviews() {
    }

    public Reviews(String CustomerImage,String CustomerName, String ReviewText, double Rating) {
        this.CustomerImage = CustomerImage;
        this.CustomerName = CustomerName;
        this.ReviewText = ReviewText;
        this.Rating = Rating;
    }

//    public Reviews(String CustomerImage,String CustomerName, String ReviewText, double Rating) {
//        this.CustomerImage = CustomerImage;
//        this.CustomerName = CustomerName;
//        this.ReviewText = ReviewText;
//        this.Rating = Rating;
//    }

    public String getCustomerImage() {
        return CustomerImage;
    }

    public void setCustomerImage(String customerImage) {
        CustomerImage = customerImage;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getReviewText() {
        return ReviewText;
    }

    public void setReviewText(String reviewText) {
        ReviewText = reviewText;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }
}
