package com.taimar198.weatherongooglemap.data.api.response;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taimar198.weatherongooglemap.data.service.DownloadImage;
import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;

public class WeatherResponse implements CurrentWeatherDataSource.OnFetchDataListener<Bitmap> {

    @Expose
    @SerializedName("description")
    private String description;
    @Expose
    @SerializedName("icon")
    private String icon;

    private Bitmap bitmap ;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getIcon() {
         return this.bitmap;
    }

    public void setIcon(Bitmap icon) {
        this.bitmap = icon;
    }

    private void fetchingImage() {
        new DownloadImage(this).execute(this.icon);
    }

    @Override
    public void onFetchDataSuccess(Bitmap data) {
        setIcon(data);
    }

    @Override
    public void onFetchDataFailure(Exception e) {

    }
}
