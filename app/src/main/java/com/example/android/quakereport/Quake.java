package com.example.android.quakereport;

/**
 * Created by Tommy on 11/13/2016.
 */
public class Quake {
    private double mag;
    private String name;
    private String dateOccured;
    public Quake(double mag, String name, String dateOccured){
        this.mag = mag;
        this.name = name;
        this.dateOccured = dateOccured;
    }


    public double getMag() {
        return mag;
    }

    public String getName() {
        return name;
    }

    public String getDateOccured() {
        return dateOccured;
    }
}
