package com.vahagn.barber_line.model;

import android.graphics.Color;

public class Category {
    int id,image;
    String title;
    String color;

    public Category(int id, String title, int image,String color) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
