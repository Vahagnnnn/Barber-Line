package com.vahagn.barber_line.FirebaseDatabaseClasses;

import java.util.Map;

class Users {
    public int Id, phoneNumber;
    public String name,password;
    public Map<String, String> appointments;

    public Users(int phoneNumber, String name, String password) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.password = password;
    }
}
