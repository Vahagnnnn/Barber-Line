package com.vahagn.barber_line.model;

public class TopHaircuts {
    private final int imageView;
    private final String name;

    public TopHaircuts(int imageView, String name) {
        this.imageView = imageView;
        this.name = name;
    }

    public int getImageView() {
        return imageView;
    }

    public String getName() {
        return name;
    }
}
