package com.mklgallegos.boggle.models;

import java.util.ArrayList;
import java.util.Date;

public class Game {

    private ArrayList<String> list;
    private String pushId;
    private Date date;
    private Integer totalPoints;

    //defining an empty constructor is necessary for saving Java objects to Firebase
    public Game(){}

    //constructor
    public Game(ArrayList<String> list, Date date) {
        this.list = list;
        this.date = date;
        this.totalPoints = totalPoints;
    }

    //getter methods
    public ArrayList<String> getList() {return list; }
    public String getPushId() {return pushId; }
    public Date getDate() {return date; }
    public Integer getTotalPoints() {return totalPoints; }

    //setter methods
    public void setPushId(String pushId) {this.pushId = pushId; }
    public void setList(ArrayList<String> list) {this.list = list; }
    public void setDate(Date date) {this.date = date; }
    public void setTotalPoints (Integer totalPoints) {this.totalPoints = totalPoints; }

}
