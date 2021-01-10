package com.taimar198.weatherongooglemap.utls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.google.gson.JsonParser;
import com.taimar198.weatherongooglemap.data.model.DailyWeather;
import com.taimar198.weatherongooglemap.data.model.HourlyWeather;
import com.taimar198.weatherongooglemap.data.model.Location;
import com.taimar198.weatherongooglemap.data.model.Weather;
import com.taimar198.weatherongooglemap.data.model.WeatherForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.taimar198.weatherongooglemap.constants.Constants.IMG_URL;

public class JSONWeatherParser {


    public static Bitmap getBitmapFromURL(String icon) {
        try {
            java.net.URL url = new java.net.URL(IMG_URL + icon + ".png");
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bmp, int newHeight, int newWidth) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

}
