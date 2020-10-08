package com.taimar198.weatherongooglemap.data.local.source;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.Task;
import com.taimar198.weatherongooglemap.data.local.GetDataHandler;
import com.taimar198.weatherongooglemap.data.local.LocalGetDataAsync;
import com.taimar198.weatherongooglemap.data.local.dao.WeatherDAO;
import com.taimar198.weatherongooglemap.data.source.WeatherDataSource;

import org.json.JSONException;

import java.util.Collections;
import java.util.List;

public class WeatherLocalDataSource implements WeatherDataSource {

    private static WeatherLocalDataSource sTaskLocalDataSource;
    private WeatherDAO mWeatherDAO;

    private WeatherLocalDataSource(WeatherDAO weatherDAO) {
        mWeatherDAO = weatherDAO;
    }

    public static WeatherLocalDataSource getInstance(WeatherDAO weatherDAO) {
        if (sTaskLocalDataSource == null) {
            sTaskLocalDataSource = new WeatherLocalDataSource(weatherDAO);
        }
        return sTaskLocalDataSource;
    }

    @Override
    public List<List<Object>> getCurrentWeather(int id) throws JSONException {
        LocalGetDataAsync<List<Object>> localGetDataAsync = new LocalGetDataAsync<List<Object>>((GetDataHandler<List<Object>>) ()
                -> Collections.singletonList(mWeatherDAO.getCurrentWeather(id)), callback);
        localGetDataAsync.execute();
    }

    @Override
    public void SaveCurrentWeather(List<GroundOverlayOptions> groundOverlayOptionsList,
                                   List<Marker> markerList, List<Bitmap> bitmap, boolean isCurrent) throws JSONException {

    }
}
