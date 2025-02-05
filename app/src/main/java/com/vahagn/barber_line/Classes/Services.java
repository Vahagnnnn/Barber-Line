package com.vahagn.barber_line.Classes;

import java.io.Serializable;

public class Services implements Serializable {
    private String name;
    private String price;
    public String duration;

    public Services() {}

    public Services(String name, String price, String duration) {
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Services{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                '}';
    }
}