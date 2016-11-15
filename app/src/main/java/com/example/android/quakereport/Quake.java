package com.example.android.quakereport;

/**
 * Created by Tommy on 11/13/2016.
 */
import android.graphics.drawable.GradientDrawable;
public class Quake {
    private String mag;
    private String name;
    private String dateOccured;
    public Quake(String mag, String name, String dateOccured){
        this.mag = mag;
        this.name = name;
        this.dateOccured = dateOccured;
    }


    public String getMag() {
        return mag;
    }

    public String getName() {
        return name;
    }

    public String getDateOccured() {
        return dateOccured;
    }
}
