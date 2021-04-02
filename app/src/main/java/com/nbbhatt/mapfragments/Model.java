package com.nbbhatt.mapfragments;

import java.io.Serializable;

public class Model implements Serializable {

    String Email, Name, Number ,Password, UserLatitude, UserLongitude;

    public Model() {
    }

    public Model(String email, String name, String number, String password, String userLatitude, String userLongitude) {
        this.Email = email;
        this.Name = name;
        this.Number = number;
        this.Password = password;
        this.UserLatitude = userLatitude;
        this.UserLongitude = userLongitude;
    }


    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }


    public String getNumber() {
        return Number;
    }


    public String getPassword() {
        return Password;
    }


    public String getUserLatitude() {
        return UserLatitude;
    }


    public String getUserLongitude() {
        return UserLongitude;
    }


}
