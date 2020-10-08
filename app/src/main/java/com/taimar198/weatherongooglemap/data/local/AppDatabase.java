package com.taimar198.weatherongooglemap.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.taimar198.weatherongooglemap.data.local.entry.WeatherEntry;
/**https://stackoverflow.com/questions/23577825/android-save-object-as-blob-in-sqlite/23578035*/
public class AppDatabase extends SQLiteOpenHelper {

    private static AppDatabase sAppDatabase;

    private static final String DATABASE_NAME = "weather_database";
    private static final int VERSION = 1;

    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " ("
            + WeatherEntry.ID + " integer primary key, "
            + WeatherEntry.MARKER + " text not null, "
            + WeatherEntry.RADAR + " text, "
            + WeatherEntry.POLYLINE + " text )";

    private static final String DROP_WEATHER_TABLE = "DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME;
    protected SQLiteDatabase mSQLiteDatabase;

    protected AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static AppDatabase getInstance(Context context) {
        if (sAppDatabase == null) {
            sAppDatabase = new AppDatabase(context);
        }
        return sAppDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_WEATHER_TABLE);
        onCreate(db);
    }
}
