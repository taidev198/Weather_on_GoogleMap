package com.taimar198.weatherongooglemap.ui.appwidget;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationListener;
import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.api.UtilsApi;
import com.taimar198.weatherongooglemap.data.api.WeatherApi;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.api.response.WeatherResponse;
import com.taimar198.weatherongooglemap.utls.Methods;

import java.net.InetAddress;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * http://amo.gov.vn/radar/
 * Implementation of App Widget functionality.
 * https://medium.com/coding-blocks/creating-a-widget-for-your-android-app-1ee915e6af3e
 * https://android--examples.blogspot.com/2015/10/android-how-to-create-weather-widget.html
 * https://www.vogella.com/tutorials/AndroidWidgets/article.html#updates
 */
public class WeatherAppWidget extends AppWidgetProvider implements LocationListener {

    public static String UPDATE_ACTION = "ActionUpdateWeatherWidget";
    private WeatherApi mWeatherApi;
    public static final String ACTION_TEXT_CHANGED = "com.taimar198.weatherongooglemap.ui.appwidget.TEXT_CHANGED";
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

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
        if (intent.getAction().equals(ACTION_TEXT_CHANGED)) {
            // handle intent here
            String s = intent.getStringExtra("NewString");
            System.out.println(s);
        }
        super.onReceive(context, intent);

        fetchingWeather(context);
    }

    private void fetchingWeather(final Context context) {
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

                            List<WeatherResponse> weatherResponseList = weatherForecastResponses
                                    .getCurrentWeather()
                                    .getWeathers();
                            remoteViews.setImageViewResource(R.id.icon_weather_widget,
                                    Methods.getDrawable(weatherResponseList.get(0).getIcon(),
                                            context));

                            remoteViews.setTextViewText(R.id.address_weather_text, weatherResponseList
                                    .get(0)
                                    .getDescription());

                            remoteViews.setTextViewText(R.id.temp_widget, String.valueOf(weatherForecastResponses
                                    .getCurrentWeather()
                                    .getTemp()));
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
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    (android.location.LocationListener) this);
        }

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
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

    @Override
    public void onLocationChanged(Location location) {
        System.out.println(location.getLatitude() + "----" +location.getLongitude());
    }

    public interface OnDataReceived {
        void receiveData(WeatherForecastResponse weatherForecastResponse);
    }
}

