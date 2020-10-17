package com.taimar198.weatherongooglemap.utls;

import android.net.Uri;
import com.taimar198.weatherongooglemap.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {
/**Weather icon: https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
 * Link get icon:http://openweathermap.org/img/wn/10d@2x.png*/
    public static String formatWeatherAPI(String lat, String lon) {
        Uri.Builder urlBuilder = new Uri.Builder();
        urlBuilder.scheme(Constants.SCHEME)
                .encodedAuthority(Constants.BASE_URL)
                .appendPath(Constants.VERSION)
                .appendPath(Constants.WEATHER)
                .appendQueryParameter(Constants.LAT, lat)
                .appendQueryParameter(Constants.LON, lon)
                .appendQueryParameter(Constants.APP_ID, "e370756ec8af6d31ce5f25668bf0bee8");
        System.out.println(urlBuilder.build().toString());
        return urlBuilder.build().toString();
    }

    public static String formatWeatherByCityName(String cityName) {
        Uri.Builder urlBuilder = new Uri.Builder();
        urlBuilder.scheme(Constants.SCHEME)
                .encodedAuthority(Constants.BASE_URL)
                .appendPath(Constants.VERSION)
                .appendPath(Constants.WEATHER)
                .appendQueryParameter(Constants.QUERY, cityName)
                .appendQueryParameter(Constants.APP_ID, "e370756ec8af6d31ce5f25668bf0bee8");
        System.out.println(urlBuilder.build().toString());
        return urlBuilder.build().toString();
    }

    public static String getWeatherForecast(String lat, String lon) {
        Uri.Builder urlBuilder = new Uri.Builder();
        urlBuilder.scheme(Constants.SCHEME)
                .encodedAuthority(Constants.BASE_URL)
                .appendPath(Constants.VERSION)
                .appendPath(Constants.ONECALL)
                .appendQueryParameter(Constants.LAT, lat)
                .appendQueryParameter(Constants.LON, lon)
                .appendQueryParameter(Constants.EXCLUDE,
                        Constants.DAILY+ Constants.COMMA + Constants.HOURLY)
                .appendQueryParameter(Constants.LANG, Constants.VI)
                .appendQueryParameter(Constants.UNITS, Constants.METRIC)
                .appendQueryParameter(Constants.APP_ID, "e370756ec8af6d31ce5f25668bf0bee8");
        System.out.println(urlBuilder.build().toString());
        return urlBuilder.build().toString();
    }

    public static int getCelsiusFromFahrenheit(int degreeF) {
        int degreeC = Math.round((degreeF - 32) / 1.8f);
        return degreeC;
    }

    public static String getFahrenheit(int degreeF) {
        return degreeF + Constants.DEGREE;
    }

    public static String getCelsius(int degreeF) {
        int degreeC = getCelsiusFromFahrenheit(degreeF);
        return degreeC + Constants.DEGREE;
    }

    public static String getTempDayF(int tempMaxF, int tempMinF) {
        return getFahrenheit(tempMaxF) + Constants.SPACE
                + Constants.SLASH + Constants.SPACE + getFahrenheit(tempMinF);
    }

    public static String getTempDayC(int tempMaxF, int tempMinF) {
        return getCelsius(tempMaxF) + Constants.SPACE
                + Constants.SLASH + Constants.SPACE + getCelsius(tempMinF);
    }

    public static String getDayInWeekFromDate(Date date) {
        SimpleDateFormat EEE = new SimpleDateFormat(Constants.FORMAT_EEE);
        String DayInWeek = EEE.format(date);
        return DayInWeek;
    }

    public static String getStringYearMonthDayFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT_YEAR_MONTH_DAY);
        String textYearMonthDay = dateFormat.format(date);
        return textYearMonthDay;
    }

    public static String getStringDayFromDate(Date date) {
        SimpleDateFormat dayFormat = new SimpleDateFormat(Constants.FORMAT_DAY);
        String textDay = dayFormat.format(date);
        return textDay;
    }

    public static String getSecondStringDayFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT_DAY_2);
        String textDayEEEEE = dateFormat.format(date);
        return textDayEEEEE;
    }

    public static String getStringHourFromDate(Date date) {
        SimpleDateFormat hourFormat = new SimpleDateFormat(Constants.FORMAT_HOUR_2);
        String textHour = hourFormat.format(date);
        return textHour;
    }
}