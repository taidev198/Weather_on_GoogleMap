package com.taimar198.weatherongooglemap.data.source.current;

import android.os.AsyncTask;

import com.taimar198.weatherongooglemap.constants.Constants;
import com.taimar198.weatherongooglemap.constants.CurrentWeatherEntity;
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;
import com.taimar198.weatherongooglemap.utls.JSONWeatherParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class FetchCurrentWeatherFromUrl extends AsyncTask<String, Void, CurrentWeather> {

    private CurrentWeatherDataSource.OnFetchDataListener mListener;
    private Exception mException;

    public FetchCurrentWeatherFromUrl(CurrentWeatherDataSource.OnFetchDataListener listener) {
        mListener = listener;
    }

    @Override
    protected CurrentWeather doInBackground(String... strings) {
        String url = strings[0];
        try {
            String data = getStringDataFromUrl(url);

            return  JSONWeatherParser.getWeather(data);
        } catch (IOException e) {
            mException = e;
        } catch (JSONException e) {
            mException = e;
        }
        return null;
    }

    private CurrentWeather getCurrentWeatherFromStringData(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);

        return null;
    }

    private String getStringDataFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(Constants.REQUEST_METHOD_GET);
        connection.setConnectTimeout(Constants.CONNECT_TIME_OUT);
        connection.setReadTimeout(Constants.READ_TIME_OUT);
        connection.setDoOutput(true);
        connection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(Constants.BREAK_LINE);
        }
        br.close();
        System.out.println("done");
        connection.disconnect();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(CurrentWeather currentWeather) {
        if (mListener == null) {
            return;
        }
        if (mException == null) {
            mListener.onFetchDataSuccess(currentWeather);
        } else {
            mListener.onFetchDataFailure(mException);
        }
    }
}
