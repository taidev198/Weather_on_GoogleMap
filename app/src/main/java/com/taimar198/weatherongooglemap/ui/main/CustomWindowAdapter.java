package com.taimar198.weatherongooglemap.ui.main;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.constants.Constants;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;

public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter{
    LayoutInflater mInflater;
   private WeatherForecastResponse mWeatherForecastResponse;

    public CustomWindowAdapter(LayoutInflater i, WeatherForecastResponse weatherForecastResponse){
        mInflater = i;
        mWeatherForecastResponse = weatherForecastResponse;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // Getting view from the layout file
        View v = mInflater.inflate(R.layout.multi_profile, null);

//        ImageView icon = v.findViewById(R.id.icon_wether);
        // Populate fields
//        TextView title =  v.findViewById(R.id.tv_info_window_title);
//        title.setText(marker.getTitle());

        TextView description =  v.findViewById(R.id.temp_marker);
        description.setText(mWeatherForecastResponse.getAddress());
//        TextView temp = v.findViewById(R.id.temp);
//        temp.setText((int) mCurrentWeather.getTemp().getTemperature()+ Constants.DEGREE);
//        TextView location = v.findViewById(R.id.location);
//        location.setText(mCurrentWeather.getLocation().getCity());
        // Return info window contents
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
