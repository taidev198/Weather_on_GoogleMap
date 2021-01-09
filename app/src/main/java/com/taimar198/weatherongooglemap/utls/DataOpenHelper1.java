package com.taimar198.weatherongooglemap.utls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataOpenHelper1 extends SQLiteOpenHelper {

    private static final String TAG = DataOpenHelper.class.getSimpleName();

    // Declaring all these as constants makes code a lot more readable, and looking like SQL.

    // Versions has to be 1 first time or app will crash.
    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME ="data.db";
    private static String DB_PATH ="";
    private static final String WORD_LIST_TABLE = "weather_data";
    private static final String DATABASE_NAME = "data";
    public static final String PASS_PHRASE = "@123";
    // Column names...
    public static final String KEY_ID = "_id";
    public static final String KEY_WORD = "data";
    private SQLiteDatabase db;
    // ... and a string array of columns.
    private static final String[] COLUMNS =
            {KEY_ID, KEY_WORD};

    // Build the SQL query that creates the table.
    private static final String WORD_LIST_TABLE_CREATE =
            "CREATE TABLE " + WORD_LIST_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " + // will auto-increment if no value passed
                    KEY_WORD + " TEXT );";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;
    private Context mContext;
    public DataOpenHelper1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = (context);
        DB_PATH = mContext.getApplicationInfo().dataDir+"databases";
        System.out.println("database"+ DB_PATH);
        Log.d(TAG, "Construct WordListOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteDatabase.loadLibs(mContext);
        File databaseFile = mContext.getDatabasePath("data.db");
        databaseFile.mkdirs();
        databaseFile.delete();
         db = SQLiteDatabase.openOrCreateDatabase(databaseFile, PASS_PHRASE, null);
     //   db.execSQL(WORD_LIST_TABLE_CREATE);
        fillDatabaseWithData(db);
        // We cannot initialize mWritableDB and mReadableDB here, because this creates an infinite
        // loop of on Create being repeatedly called.
    }

    public void createDB() {
        boolean isExistDB = checkExist();
        if (!isExistDB) {
            this.getReadableDatabase(PASS_PHRASE);
            this.close();
            try {
                copyDB();
            } catch (Exception exception) {

            }
        }
    }

    public void copyDB() {
        InputStream inputStream = null;
        try{
            inputStream = mContext.getAssets().open(DB_NAME);
            String outputFileName = new StringBuilder(DB_PATH).append(DB_NAME).toString();
            OutputStream outputStream = new FileOutputStream(outputFileName);
            byte[] mBuffer = new byte[1024];
           int length;
            while ((length = inputStream.read(mBuffer)) >0){
                outputStream.write(mBuffer,0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void closeDB(){
        db.close();
    }

    public boolean openDB() {
        String path = new StringBuilder((DB_PATH)).append(DB_NAME).toString();
        System.out.println(path);
        db = SQLiteDatabase.openDatabase(path, PASS_PHRASE, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return db!=null;
    }
    private boolean checkExist() {
        File dbfile = new File(DB_PATH + DB_NAME);
        return dbfile.exists();
    }

    /**
     * Adds the initial data set to the database.
     * According to the docs, onCreate for the open helper does not run on the UI thread.
     *
     * @param db Database to fill with data since the member variables are not initialized yet.
     */
    public void fillDatabaseWithData(SQLiteDatabase db) {

//        String[] words = {"Android", "Adapter", "ListView", "AsyncTask", "Android Studio",
//                "SQLiteDatabase", "SQLOpenHelper", "Data model", "ViewHolder",
//                "Android Performance", "OnClickListener"};
//
//        // Create a container for the data.
//        ContentValues values = new ContentValues();
//
//        for (int i=0; i < words.length;i++) {
//            // Put column/value pairs into the container. put() overwrites existing values.
//            values.put(KEY_WORD, words[i]);
//            db.insert(WORD_LIST_TABLE, null, values);
//        }
    }

    /**
     * Queries the database for an entry at a given position.
     *
     * @param position The Nth row in the table.
     * @return a WordItem with the requested database entry.
     */
    public WeatherForecastResponse query(int position) {
        String query = "SELECT  * FROM " + WORD_LIST_TABLE +
                " ORDER BY " + KEY_WORD + " ASC " +
                "LIMIT " + position + ",1";

        Cursor cursor = null;
        WeatherForecastResponse entry = new WeatherForecastResponse();

        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase(PASS_PHRASE);}
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            byte[] blob = cursor.getBlob(cursor.getColumnIndex(KEY_WORD));
            String json = new String(blob);
            Gson gson = new Gson();
            WeatherForecastResponse weatherForecastResponses = gson.fromJson(json,
                    new TypeToken<WeatherForecastResponse>()
                    {}.getType());
            entry.setData(weatherForecastResponses);
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            // Must close cursor and db now that we are done with it.
            cursor.close();
            return entry;
        }
    }

    /**
     * Gets the number of rows in the word list table.
     *
     * @return The number of entries in WORD_LIST_TABLE.
     */
    public long count() {
        if (mReadableDB == null) {mReadableDB = getReadableDatabase(PASS_PHRASE);}
        return DatabaseUtils.queryNumEntries(mReadableDB, WORD_LIST_TABLE);
    }

    /**
     * Adds a single word row/entry to the database.
     *
     * @param  weatherForecastResponse New word.
     * @return The id of the inserted word.
     */
    public long insert(WeatherForecastResponse weatherForecastResponse) {
        long newId = 0;
        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, gson.toJson(weatherForecastResponse).getBytes());
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase(PASS_PHRASE);}
            newId = mWritableDB.insert(WORD_LIST_TABLE, null, values);
            System.out.println("address" + query(3).getLat());
            mWritableDB.close();
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return newId;
    }

    /**
     * Updates the word with the supplied id to the supplied value.
     *
     * @param id Id of the word to update.
     * @param word The new value of the word.
     * @return The number of rows affected or -1 of nothing was updated.
     */
    public int update(int id, String word) {
        int mNumberOfRowsUpdated = -1;
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase(PASS_PHRASE);}
            ContentValues values = new ContentValues();
            values.put(KEY_WORD, word);

            mNumberOfRowsUpdated = mWritableDB.update(WORD_LIST_TABLE, //table to change
                    values, // new values to insert
                    KEY_ID + " = ?", // selection criteria for row (in this case, the _id column)
                    new String[]{String.valueOf(id)}); //selection args; the actual value of the id

        } catch (Exception e) {
            Log.d (TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }
        return mNumberOfRowsUpdated;
    }

    /**
     * Deletes one entry identified by its id.
     *
     * @param id ID of the entry to delete.
     * @return The number of rows deleted. Since we are deleting by id, this should be 0 or 1.
     */
    public int delete(int id) {
        int deleted = 0;
        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase(PASS_PHRASE);}
            deleted = mWritableDB.delete(WORD_LIST_TABLE, //table name
                    KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());        }
        return deleted;
    }

    /**
     * Called when a database needs to be upgraded. The most basic version of this method drops
     * the tables, and then recreates them. All data is lost, which is why for a production app,
     * you want to back up your data first. If this method fails, changes are rolled back.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + WORD_LIST_TABLE);
        onCreate(db);
    }
}