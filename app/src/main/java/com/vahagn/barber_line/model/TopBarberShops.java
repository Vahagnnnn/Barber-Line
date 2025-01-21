package com.vahagn.barber_line.model;

public class TopBarberShops {
    private final int imageResId;
    private final String title;
    private final String address;
    private final String time;

    public TopBarberShops(int imageResId, String title, String address, String time) {
        this.imageResId = imageResId;
        this.title = title;
        this.address = address;
        this.time = time;
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

    public String getTime() {
        return time;
    }
}
