package com.vahagn.barber_line.Firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class BarberShopsDetail {
    private int ID;
    private String Address;
    private String Image; // Change to String
    private String Logo;
    private String Name;
    private double Rating;
    private String Reviews;
    private String Services;
    private String Specialists;

    public BarberShopsDetail() {}

    public BarberShopsDetail(int ID, String Address, String Image, String Logo, String Name, double Rating,
                             String Reviews, String Services, String Specialists) {
        this.ID = ID;
        this.Address = Address;
        this.Image = Image;
        this.Logo = Logo;
        this.Name = Name;
        this.Rating = Rating;
        this.Reviews = Reviews;
        this.Services = Services;
        this.Specialists = Specialists;
    }

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getAddress() { return Address; }
    public void setAddress(String Address) { this.Address = Address; }

    public String getImage() { return Image; }
    public void setImage(String Image) { this.Image = Image; }

    public String getLogo() { return Logo; }
    public void setLogo(String Logo) { this.Logo = Logo; }

    public String getName() { return Name; }
    public void setName(String Name) { this.Name = Name; }

    public double getRating() { return Rating; }
    public void setRating(double Rating) { this.Rating = Rating; }

    public String getReviews() { return Reviews; }
    public void setReviews(String Reviews) { this.Reviews = Reviews; }

    public String getServices() { return Services; }
    public void setServices(String Services) { this.Services = Services; }

    public String getSpecialists() { return Specialists; }
    public void setSpecialists(String Specialists) { this.Specialists = Specialists; }
}

