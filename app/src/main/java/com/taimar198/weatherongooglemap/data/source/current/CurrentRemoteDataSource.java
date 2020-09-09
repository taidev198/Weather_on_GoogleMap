package com.taimar198.weatherongooglemap.data.source.current;

import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;
import com.taimar198.weatherongooglemap.utls.StringUtil;

public class CurrentRemoteDataSource implements CurrentWeatherDataSource.RemoteDataSource {
    private static CurrentRemoteDataSource sInstance;

    private CurrentRemoteDataSource() {
    }

    public static CurrentRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new CurrentRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public void getCurrentWeather(CurrentWeatherDataSource.OnFetchDataListener listener,
                                  String lat, String lon) {
        FetchCurrentWeatherFromUrl fetchCurrentWeatherFromUrl
                = new FetchCurrentWeatherFromUrl(listener);
        fetchCurrentWeatherFromUrl.execute(StringUtil.formatWeatherAPI(lat, lon));
    }
}
