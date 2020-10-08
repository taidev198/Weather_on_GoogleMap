package com.taimar198.weatherongooglemap.data.local.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.local.AppDatabase;
import com.taimar198.weatherongooglemap.data.local.entry.WeatherEntry;
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**https://stackoverflow.com/questions/5703330/saving-arraylists-in-sqlite-databases*/
public class WeatherDAOImpl extends AppDatabase implements WeatherDAO {

    private static WeatherDAOImpl sWeatherDAOImpl;
    private static int WEATHER_ID_CURRENT = 1;
    private static int WEATHER_ID_SEARCH = 2;

    private WeatherDAOImpl(Context context, WeatherDAOImpl weatherDAO) {
        super(context);
        sWeatherDAOImpl = weatherDAO;
    }

    public static WeatherDAOImpl getInstance(Context context, WeatherDAOImpl weatherDAO) {
        if (sWeatherDAOImpl == null) {
            sWeatherDAOImpl = new WeatherDAOImpl(context, weatherDAO);
        }
        return sWeatherDAOImpl;
    }


    @Override
    public List<List<Object>> getCurrentWeather(int id) throws JSONException {
        mSQLiteDatabase = getReadableDatabase();
        List<List<Object>> result = new ArrayList<>();
        Gson gson = new Gson();
        String where = WeatherEntry.ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = mSQLiteDatabase.query(WeatherEntry.TABLE_NAME,
                null, where,
                whereArgs, null, null, null);
        byte[] blob = cursor.getBlob(cursor.getColumnIndex(WeatherEntry.MARKER));
        String json = new String(blob);
        List<Marker> markerList = gson.fromJson(json, new TypeToken<ArrayList<Marker>>(){}.getType());
        blob = cursor.getBlob(cursor.getColumnIndex(WeatherEntry.POLYLINE));
        List<GroundOverlayOptions> groundOverlayOptions = gson.fromJson(json, new TypeToken<ArrayList<GroundOverlayOptions>>(){}.getType());
        List<Bitmap> bitmap = gson.fromJson(json, new TypeToken<List<Bitmap>>(){}.getType());
        result.add(Collections.singletonList(markerList));
        result.add(Collections.singletonList(groundOverlayOptions));
        result.add(Collections.singletonList(bitmap));
        return result;
    }

    @Override
    public void SaveCurrentWeather(List<GroundOverlayOptions> groundOverlayOptionsList,
                                   List<Marker> markerList, List<Bitmap> bitmap, boolean isCurrent) throws JSONException {
        mSQLiteDatabase = getWritableDatabase();
        Gson gson = new Gson();
        int WEATHER_ID = isCurrent? WEATHER_ID_CURRENT: WEATHER_ID_SEARCH;
        String where = WeatherEntry.ID + " = ?";
        String[] whereArgs = {String.valueOf(WEATHER_ID)};
        ContentValues values = new ContentValues();
        values.put(WeatherEntry.ID, WEATHER_ID);
        values.put(WeatherEntry.MARKER, gson.toJson(markerList).getBytes());
        values.put(WeatherEntry.POLYLINE, gson.toJson(groundOverlayOptionsList).getBytes());
        values.put(WeatherEntry.DATA, gson.toJson(bitmap).getBytes());
        int result = mSQLiteDatabase.update(WeatherEntry.TABLE_NAME, values, where, whereArgs);
        mSQLiteDatabase.close();
    }
}
