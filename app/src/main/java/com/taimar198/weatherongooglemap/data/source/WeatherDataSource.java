package com.taimar198.weatherongooglemap.data.source;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;

import java.util.List;

public interface WeatherDataSource {

    List<List<Object>> getCurrentWeather(int id) throws JSONException;
    void SaveCurrentWeather(List<GroundOverlayOptions> groundOverlayOptionsList,
                            List<Marker> markerList, List<Bitmap> bitmap, boolean isCurrent) throws JSONException;
}
