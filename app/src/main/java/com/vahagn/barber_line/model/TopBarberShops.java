package com.vahagn.barber_line.model;

import android.util.Log;

public class TopBarberShops {
    private final int imageResId;
    private final String title, address;

    public TopBarberShops(int imageResId, String title, String address) {
        this.imageResId = imageResId;
        this.title = title;
        this.address = address;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }
}
