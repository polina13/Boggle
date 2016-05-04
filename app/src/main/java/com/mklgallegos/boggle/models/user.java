package com.mklgallegos.boggle.models;

public class User {
    private String firstName;
    private String lastName;
    private String email;

    //defining an empty constructor is necessary for saving Java objects to Firebase
    public User(){}

    //constructor
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    //getter methods
    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmail(){
        return email;
    }
}
