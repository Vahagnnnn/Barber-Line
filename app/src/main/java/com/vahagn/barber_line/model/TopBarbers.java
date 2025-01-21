package com.vahagn.barber_line.model;

public class TopBarbers {
    private final int imageView;
    private final String name, phone;

    public TopBarbers(int imageResId, String name, String phone) {
        this.imageView = imageResId;
        this.name = name;
        this.phone = phone;
    }

    public int getImageView() {
        return imageView;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
