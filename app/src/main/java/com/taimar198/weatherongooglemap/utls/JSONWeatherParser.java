package com.taimar198.weatherongooglemap.utls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.taimar198.weatherongooglemap.data.model.CurrentWeather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static com.taimar198.weatherongooglemap.constants.Constants.IMG_URL;

public class JSONWeatherParser {

    public final static String COORDINATES = "coord",
            LATITUDE = "lat",
            LONGITUDE = "lon",
            EXTRAS = "sys",
            WEATHER = "weather",
            WEATHER_TITLE = "main",
            WEATHER_DESCRIPTION = "description",
            EXTRAS_COUNTRY = "country",
            CITY = "name",
            WEATHER_ICON_STRING = "icon",
            CURRENT = "main",
            CURRENT_TEMPERATURE = "temp",
            CURRENT_HUMIDITY = "humidity",
            CURRENT_PRESSURE = "pressure",
            CURRENT_MAX_TEMPERATURE = "temp_max",
            CURRENT_MIN_TEMPERATURE = "temp_min",
            WIND = "wind",
            WIND_SPEED = "speed",
            WIND_DEGREES = "deg",
            CLOUDS = "clouds",
            CLOUDINESS = "all";

    public static CurrentWeather getWeather(String data) throws JSONException {
        JSONObject mainObj = new JSONObject(data);
        JSONObject coordinatesObj = getObject(COORDINATES, mainObj);
//        JSONObject mainWeatherObj = getObject(WEATHER_TITLE, mainObj);
        JSONObject tempObj = getObject(WEATHER_TITLE, mainObj);
        JSONObject extrasObj = getObject(EXTRAS, mainObj);
        JSONObject weatherObj = mainObj.getJSONArray(WEATHER).getJSONObject(0);
        JSONObject currentOjb = getObject(CURRENT, mainObj);
        JSONObject cloudsObj = getObject(CLOUDS, mainObj);
        JSONObject windObj = getObject(WIND, mainObj);

        CurrentWeather weather = new CurrentWeather();
        // Location data
        weather.getLocation().setCity(getString(CITY, mainObj));
        weather.getLocation().setLatitude(getFloat(LATITUDE, coordinatesObj));
        weather.getLocation().setLongitude(getFloat(LONGITUDE, coordinatesObj));
        weather.getLocation().setCountry(getString(EXTRAS_COUNTRY, extrasObj));
        weather.setWeather(getString(WEATHER_DESCRIPTION, weatherObj));
        weather.setIcon(getBitmapFromURL(getString(WEATHER_ICON_STRING, weatherObj)));
//        // Current condition
//        weather.getCurrentCondition().setDescription(getString(WEATHER_DESCRIPTION, weatherObj));
//        weather.getCurrentCondition().setCondition(getString(WEATHER_TITLE, weatherObj));
//        weather.getCurrentCondition().setIcon(getString(WEATHER_ICON_STRING, weatherObj));
//        weather.getCurrentCondition().setHumidity(getInt(CURRENT_HUMIDITY, currentOjb));
//        weather.getCurrentCondition().setPressure(getInt(CURRENT_PRESSURE, currentOjb));
//
//        // Temperature data
        weather.getTemp().setMaxTemperature(getFloat(CURRENT_MAX_TEMPERATURE, tempObj));
        weather.getTemp().setMinTemperature(getFloat(CURRENT_MIN_TEMPERATURE, tempObj));
        weather.getTemp().setTemperature(getFloat(CURRENT_TEMPERATURE, tempObj));
//
//        // Wind data
//        weather.getWind().setSpeed(getFloat(WIND_SPEED, windObj));
//        weather.getWind().setDegrees(getFloat(WIND_DEGREES, windObj));
//
//        // Clouds data
//        weather.getClouds().setPrecipitation(getInt(CLOUDINESS, cloudsObj));
        System.out.println("done6");
        return weather;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        return jObj.getJSONObject(tagName);
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

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
