package com.taimar198.weatherongooglemap.utls;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;

import net.sqlcipher.Cursor;
import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static DataBaseHelper instance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "weather_data";
    public static final String TABLE_NAME = "weather";
    private static final String KEY_ID = "_id";
    private static final String CONTENT = "content";
    private static final String PASS = "123";
    private static final String WORD_LIST_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " + // will auto-increment if no value passed
                    CONTENT + " TEXT );";

    private SQLiteDatabase sqLiteDatabase;
    private SQLiteDatabase mWritableDB;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static public synchronized DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(WORD_LIST_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(WORD_LIST_TABLE_CREATE);
        onCreate(db);

    }

    public long count() {
        if (sqLiteDatabase == null) {sqLiteDatabase = getReadableDatabase(PASS);}
        return DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);
    }

    public long insert(WeatherForecastResponse weatherForecastResponse) {
        long newId = 0;
        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(CONTENT, gson.toJson(weatherForecastResponse).getBytes());
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase(PASS);}
            newId = mWritableDB.insert(TABLE_NAME, null, values);
            mWritableDB.close();
            getData();
        } catch (Exception e) {
            System.out.println( e.getMessage() + "error");
        }

        return newId;
    }

    private void getData() {
        SQLiteDatabase db = getWritableDatabase(PASS);
        Cursor cursor = db.rawQuery("SELECT * FROM '" + TABLE_NAME + "';", null);
        System.out.println("day la hinh anh" + cursor.getString(1));
        String dbValues = "";

        if (cursor.moveToFirst()) {
            do {
                dbValues = dbValues + "\n" + cursor.getString(0) + " , " + cursor.getString(1);
                System.out.println("day la hinh anh" +dbValues);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
}
