package com.taimar198.weatherongooglemap.data.local.dao;

import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;

public interface WeatherDAO {

    CurrentWeather getCurrentWeather();
    void SaveCurrentWeather(WeatherForecastResponse weatherForecastResponse);

}
