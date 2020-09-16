package com.taimar198.weatherongooglemap.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.api.UtilsApi;
import com.taimar198.weatherongooglemap.data.api.WeatherApi;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.model.Weather;
import com.taimar198.weatherongooglemap.data.model.WeatherForecast;
import com.taimar198.weatherongooglemap.data.repository.CurrentWeatherRepository;
import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;
import com.taimar198.weatherongooglemap.ui.main.CardAdapter;
import com.taimar198.weatherongooglemap.utls.Methods;

import java.net.InetAddress;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Implementation of App Widget functionality.
 * https://medium.com/coding-blocks/creating-a-widget-for-your-android-app-1ee915e6af3e
 * https://android--examples.blogspot.com/2015/10/android-how-to-create-weather-widget.html
 */
public class WeatherAppWidget extends AppWidgetProvider{

    private CurrentWeatherRepository mCurrentWeatherRepository;
    private static Weather weather;
    private WeatherApi mWeatherApi;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_app_widget);
        /** PendingIntent to launch the MainActivity when the widget was clicked **/
        Intent intent = new Intent(context, WeatherAppWidget.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.date_weather_text, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        super.onReceive(context, intent);

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
         final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_app_widget);
        final ComponentName watchWidget = new ComponentName(context, WeatherAppWidget.class);

        Toast.makeText(context, "Requested", Toast.LENGTH_SHORT).show();
        if (isInternetConnected()) {
            mWeatherApi = UtilsApi.getAPIService();
            mWeatherApi.requestRepos("15",
                    "105",
                    "hourly,daily",
                    "vi",
                    "metric",
                    "e370756ec8af6d31ce5f25668bf0bee8").subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<WeatherForecastResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(WeatherForecastResponse weatherForecastResponses) {
                            // Display weather data on widget
                            remoteViews.setTextViewText(R.id.date_weather_text, Methods.formatDate());

                            remoteViews.setImageViewResource(R.id.icon_weather_widget,
                                    Methods.getDrawable(weatherForecastResponses.getCurrentWeather().getWeathers().get(0).getIcon(),
                                    context));

                            remoteViews.setTextViewText(R.id.address_weather_text, weatherForecastResponses
                                    .getCurrentWeather()
                                    .getWeathers()
                                    .get(0)
                                    .getDescription());
                            // Apply the changes
                            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
                        }

                        @Override
                        public void onError(Throwable e) {
                            System.out.println(e.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
    }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
//        StrictMode.setThreadPolicy(policy);
//        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_app_widget);
//        remoteViews.setTextViewText(R.id.date_weather_text, "hihi");
//        ComponentName watchWidget = new ComponentName(context, WeatherAppWidget.class);
//
//        Toast.makeText(context, "Requested", Toast.LENGTH_SHORT).show();
//        if (isInternetConnected()) {
//            mWeatherApi = UtilsApi.getAPIService();
//            mWeatherApi.requestRepos("15",
//                    "105",
//                    "hourly,daily",
//                    "vi",
//                    "metric",
//                    "e370756ec8af6d31ce5f25668bf0bee8").subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<WeatherForecastResponse>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(WeatherForecastResponse weatherForecastResponses) {
//                            System.out.println(weatherForecastResponses
//                                    .getCurrentWeather()
//                                    .getWeathers()
//                                    .get(0)
//                                    .getDescription());
//                            remoteViews.setTextViewText(R.id.address_weather_text, weatherForecastResponses
//                                    .getCurrentWeather()
//                                    .getWeathers()
//                                    .get(0)
//                                    .getDescription());
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            System.out.println(e.toString());
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//        }

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

        mCurrentWeatherRepository = CurrentWeatherRepository.getInstance();
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
//        views.setRemoteAdapter(R.id.widget_list,
//                new Intent(context, WidgetService.class));
    }
    // Custom method to check internet connection
    public Boolean isInternetConnected(){
        boolean status = false;
        try{
            InetAddress address = InetAddress.getByName("google.com");

            if(address!=null)
            {
                status = true;
            }
        }catch (Exception e) // Catch the exception
        {
            e.printStackTrace();
        }
        return status;
    }
}

