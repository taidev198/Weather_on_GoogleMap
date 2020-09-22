package com.taimar198.weatherongooglemap.ui.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.widget.RemoteViews;
import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import java.net.InetAddress;

/**
 * http://amo.gov.vn/radar/
 * Implementation of App Widget functionality.
 * https://medium.com/coding-blocks/creating-a-widget-for-your-android-app-1ee915e6af3e
 * https://android--examples.blogspot.com/2015/10/android-how-to-create-weather-widget.html
 * https://www.vogella.com/tutorials/AndroidWidgets/article.html#updates
 */
public class WeatherAppWidget extends AppWidgetProvider {

    public static String UPDATE_ACTION = "ActionUpdateWeatherWidget";
    public static final String ACTION_TEXT_CHANGED = "com.taimar198.weatherongooglemap.ui.appwidget.TEXT_CHANGED";

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
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        context.startService(new Intent(context,GPSWidgetService.class));
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

    public interface OnDataReceived {
        void receiveData(WeatherForecastResponse weatherForecastResponse);
    }
}

