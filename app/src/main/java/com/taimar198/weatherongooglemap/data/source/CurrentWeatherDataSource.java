package com.taimar198.weatherongooglemap.data.source;

import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;

public interface CurrentWeatherDataSource {

    interface OnFetchDataListener<T> {
        void onFetchDataSuccess(T data);

        void onFetchDataFailure(Exception e);
    }

    interface LocalDataSource {
        CurrentWeather getCurrentWeather();
        void SaveCurrentWeather(WeatherForecastResponse weatherForecastResponse);
    }

    interface RemoteDataSource {
        void getWeatherForeCast(OnFetchDataListener listener, String lat, String lon);

        void getCurrentWeatherByCityName(OnFetchDataListener listener, String cityName);
    }

}
