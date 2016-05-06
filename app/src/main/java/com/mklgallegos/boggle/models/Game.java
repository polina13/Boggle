package com.mklgallegos.boggle.models;

import java.util.ArrayList;

public class Game {

    private ArrayList<String> list;
    private String pushId;

    //defining an empty constructor is necessary for saving Java objects to Firebase
    public Game(){}

    //constructor
    public Game(ArrayList<String> list) {
        this.list = list;
    }

    //getter methods
    public ArrayList<String> getList() {return list; }
    public String getPushId() {return pushId; }

    //setter methods
    public void setPushId(String pushId) {this.pushId = pushId; }
    public void setList(ArrayList<String> list) {this.list = list; }

}
