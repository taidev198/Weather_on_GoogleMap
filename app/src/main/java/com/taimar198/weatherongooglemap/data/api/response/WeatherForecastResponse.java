package com.taimar198.weatherongooglemap.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.data.model.DailyWeather;
import com.taimar198.weatherongooglemap.data.model.HourlyWeather;
import com.taimar198.weatherongooglemap.data.model.Weather;

import java.util.List;

public class WeatherForecastResponse {

    @Expose
    @SerializedName("lat")
    private float lat;
    @Expose
    @SerializedName("lon")
    private float lon;
    @Expose
    @SerializedName("current")
    private CurrentWeatherResponse currentWeather;
    @Expose
    @SerializedName("daily")
    private List<DailyWeatherResponse> dailyWeatherList;
    @Expose
    @SerializedName("hourly")
    private List<CurrentWeatherResponse> hourlyWeatherList;

    private String Address;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public CurrentWeatherResponse getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeatherResponse currentWeather) {
        this.currentWeather = currentWeather;
    }

    public List<DailyWeatherResponse> getDailyWeatherList() {
        return dailyWeatherList;
    }

    public void setDailyWeatherList(List<DailyWeatherResponse> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    public List<CurrentWeatherResponse> getHourlyWeatherList() {
        return hourlyWeatherList;
    }

    public void setHourlyWeatherList(List<CurrentWeatherResponse> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }
}
