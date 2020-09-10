package com.taimar198.weatherongooglemap.data.model;

import android.graphics.Bitmap;

import java.util.Date;

public class CurrentWeather {
    private Date mDate;
    private Temperature mTemp;
    private String mWeather;
    private Location location;
    // Downloaded separately
    private Bitmap mIcon;


    public CurrentWeather() {
        location = new Location();
    }

    public CurrentWeather(Date date, Temperature temp, String weather, Bitmap icon) {
        mDate = date;
        mTemp = temp;
        mWeather = weather;
        mIcon = icon;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Temperature getTemp() {
        return mTemp;
    }

    public void setTemp(Temperature temp) {
        mTemp = temp;
    }

    public String getWeather() {
        return mWeather;
    }

    public void setWeather(String weather) {
        mWeather = weather;
    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }
}