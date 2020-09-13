package com.taimar198.weatherongooglemap.data.api;

import com.taimar198.weatherongooglemap.constants.Constants;

public class UtilsApi {

    private static String BASE_URL = "https://api.openweathermap.org/";

    public static WeatherApi getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(WeatherApi.class);
    }

}
