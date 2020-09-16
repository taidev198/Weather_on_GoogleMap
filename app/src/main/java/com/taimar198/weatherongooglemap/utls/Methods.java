package com.taimar198.weatherongooglemap.utls;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.api.UtilsApi;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Methods {

    public static void fetchingDataUsingRetrofit() {
//        mWeatherApi = UtilsApi.getAPIService();
//        mWeatherApi.requestRepos("15",
//                "105",
//                "hourly,daily",
//                "vi",
//                "metric",
//                "e370756ec8af6d31ce5f25668bf0bee8").subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<WeatherForecastResponse>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(WeatherForecastResponse weatherForecastResponses) {
//                        // Display weather data on widget
//                        remoteViews.setTextViewText(R.id.address_weather_text, weatherForecastResponses
//                                .getCurrentWeather()
//                                .getWeathers()
//                                .get(0)
//                                .getDescription());
//                        // Apply the changes
//                        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println(e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    public static String formatDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static int getDrawable(String name, Context context) {
        return context.getResources().getIdentifier("icon_" + name,
                "drawable",
                context.getPackageName());
    }

}
