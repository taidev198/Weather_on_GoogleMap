package com.taimar198.weatherongooglemap.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.taimar198.weatherongooglemap.R;

public class DataProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext = null;

    public DataProvider(Context context, Intent intent) {
        mContext  =context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.weather_app_widget);
        view.setTextViewText(R.id.date_weather_text, "ha noi");
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
