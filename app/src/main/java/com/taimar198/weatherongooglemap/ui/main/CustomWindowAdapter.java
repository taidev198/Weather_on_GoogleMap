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
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;

public class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter{
    LayoutInflater mInflater;
   private CurrentWeather mCurrentWeather;

    public CustomWindowAdapter(LayoutInflater i, CurrentWeather currentWeather){
        mInflater = i;
        mCurrentWeather = currentWeather;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // Getting view from the layout file
        View v = mInflater.inflate(R.layout.activity_main2, null);

        ImageView icon = v.findViewById(R.id.icon_wether);
        icon.setImageBitmap(mCurrentWeather.getIcon());
        // Populate fields
//        TextView title =  v.findViewById(R.id.tv_info_window_title);
//        title.setText(marker.getTitle());

        TextView description =  v.findViewById(R.id.des_text);
        description.setText(mCurrentWeather.getWeather());
//        TextView temp = v.findViewById(R.id.temp);
//        temp.setText((int) mCurrentWeather.getTemp().getTemperature()+ Constants.DEGREE);
        TextView location = v.findViewById(R.id.location);
        location.setText(mCurrentWeather.getLocation().getCity());
        // Return info window contents
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
