package com.taimar198.weatherongooglemap.data.source;

import com.taimar198.weatherongooglemap.data.model.WeatherForecast;

public interface CurrentWeatherDataSource {

    interface OnFetchDataListener {
        void onFetchDataSuccess(WeatherForecast data);

        void onFetchDataFailure(Exception e);
    }

    interface RemoteDataSource {
        void getWeatherForeCast(OnFetchDataListener listener, String lat, String lon);

        void getCurrentWeatherByCityName(OnFetchDataListener listener, String cityName);
    }

}
