package com.taimar198.weatherongooglemap.data.source;

import com.taimar198.weatherongooglemap.data.model.CurrentWeather;

public interface CurrentWeatherDataSource {

    interface OnFetchDataListener {
        void onFetchDataSuccess(CurrentWeather data);

        void onFetchDataFailure(Exception e);
    }

    interface RemoteDataSource {
        void getCurrentWeather(OnFetchDataListener listener, String lat, String lon);

        void getCurrentWeatherByCityName(OnFetchDataListener listener, String cityName);
    }

}
