package com.taimar198.weatherongooglemap.data.service;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;
import com.taimar198.weatherongooglemap.utls.JSONWeatherParser;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    private Exception mException;
    private CurrentWeatherDataSource.OnFetchDataListener<Bitmap> mListener;
    public DownloadImage(CurrentWeatherDataSource.OnFetchDataListener<Bitmap> listener) {
        mListener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        return JSONWeatherParser.getBitmapFromURL(strings[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mException == null) {

            mListener.onFetchDataSuccess(bitmap);
        }
        else {
            mListener.onFetchDataFailure(mException);
        }
    }
}
