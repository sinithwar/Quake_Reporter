package com.example.android.quakereport;

/**
 * Created by Tommy on 11/13/2016.
 */
public class Quake {
    private String mag;
    private String name;
    private String dateOccured;
    private String url;
    public Quake(String mag, String name, String dateOccured, String url){
        this.mag = mag;
        this.name = name;
        this.dateOccured = dateOccured;
        this.url = url;
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

    public String getUrl() {
        return url;
    }
}
