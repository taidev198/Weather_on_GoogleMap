package com.taimar198.weatherongooglemap.data.repository;

import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;
import com.taimar198.weatherongooglemap.data.source.current.CurrentRemoteDataSource;

public class CurrentWeatherRepository {
    private static CurrentWeatherRepository sInstance;
    private CurrentRemoteDataSource mCurrentRemoteDataSource;

    private CurrentWeatherRepository(CurrentRemoteDataSource currentRemoteDataSource) {
        mCurrentRemoteDataSource = currentRemoteDataSource;
    }

    public static CurrentWeatherRepository getInstance() {
        if (sInstance == null) {
            sInstance = new CurrentWeatherRepository(CurrentRemoteDataSource.getInstance());
        }
        return sInstance;
    }

    public void getCurrentWeather(CurrentWeatherDataSource.OnFetchDataListener listener,
                                  String lat, String lon) {
        mCurrentRemoteDataSource.getWeatherForeCast(listener, lat, lon);
    }

    public void getCurrentWeatherByCityName(CurrentWeatherDataSource.OnFetchDataListener listener,
                                  String cityName) {
        mCurrentRemoteDataSource.getCurrentWeatherByCityName(listener, cityName);
    }
}