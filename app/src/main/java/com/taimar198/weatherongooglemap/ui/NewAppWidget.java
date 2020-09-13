package com.taimar198.weatherongooglemap.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.model.WeatherForecast;
import com.taimar198.weatherongooglemap.data.repository.CurrentWeatherRepository;
import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider implements CurrentWeatherDataSource.OnFetchDataListener {

    private CurrentWeatherRepository mCurrentWeatherRepository;
    private static WeatherForecast mWeatherForecast;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, mWeatherForecast.getCurrentWeather().getWeather());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        mCurrentWeatherRepository = CurrentWeatherRepository.getInstance();
        mCurrentWeatherRepository.getCurrentWeather(this, "21.027763", "105.834160");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onFetchDataSuccess(WeatherForecast data) {
        mWeatherForecast = data;
    }

    @Override
    public void onFetchDataFailure(Exception e) {

    }
}

