package com.vahagn.barber_line.Firebase;

public class BarberShops {
    private int imageResId;
    private String name, address;

    public BarberShops() {
    }

    public BarberShops(int imageResId, String name, String address) {
        this.imageResId = imageResId;
        this.name = name;
        this.address = address;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
