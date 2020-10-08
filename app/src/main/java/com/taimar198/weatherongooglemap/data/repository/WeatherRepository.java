package com.taimar198.weatherongooglemap.data.repository;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.Marker;
import com.taimar198.weatherongooglemap.data.source.WeatherDataSource;

import org.json.JSONException;

import java.util.List;

public class WeatherRepository implements WeatherDataSource {

    private static WeatherRepository sWeatherRepository;
    private WeatherDataSource mWeatherDataSource;

    private WeatherRepository(WeatherDataSource taskDataSource) {
        mWeatherDataSource = taskDataSource;
    }

    public static WeatherRepository getInstance(WeatherDataSource weatherDataSource) {

        if (sWeatherRepository == null) {
            sWeatherRepository = new WeatherRepository(weatherDataSource);
        }
        return sWeatherRepository;
    }


    @Override
    public List<List<Object>> getCurrentWeather(int id) throws JSONException {
        return mWeatherDataSource.getCurrentWeather(id);
    }

    @Override
    public void SaveCurrentWeather(List<GroundOverlayOptions> groundOverlayOptionsList, List<Marker> markerList, List<Bitmap> bitmap, boolean isCurrent) throws JSONException {
        mWeatherDataSource.SaveCurrentWeather(groundOverlayOptionsList, markerList, bitmap, isCurrent);
    }
}
