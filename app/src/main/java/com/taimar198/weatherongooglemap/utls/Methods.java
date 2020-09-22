package com.taimar198.weatherongooglemap.utls;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.constants.Constants;
import com.taimar198.weatherongooglemap.data.api.UtilsApi;
import com.taimar198.weatherongooglemap.data.api.WeatherApi;
import com.taimar198.weatherongooglemap.data.api.response.GeocodingResponse;
import com.taimar198.weatherongooglemap.data.api.response.GeometryResponse;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.api.response.WeatherResponse;
import com.taimar198.weatherongooglemap.data.model.PlaceMark;
import com.taimar198.weatherongooglemap.data.model.PlaceMarkList;
import com.taimar198.weatherongooglemap.ui.appwidget.WeatherAppWidget;
import com.taimar198.weatherongooglemap.ui.base.OnGetData;
import com.taimar198.weatherongooglemap.ui.main.CardAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Methods {


    public static void fetchingWeatherForecast(WeatherApi weatherApi, String lat, String lon, String address) {
        weatherApi.requestRepos(lat,
                lon,
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
//                        mWeatherForecastResponse = weatherForecastResponses;
//                        pagerAdapter = new CardAdapter(getSupportFragmentManager(),weatherForecastResponses,2);
//                        pagerAdapter.notifyDataSetChanged();
//                        mPager.setAdapter(pagerAdapter);
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


    public static void getCoorsFromAddress(WeatherApi weatherApi, String province, String district, OnGetData<LatLng> listener) {
        weatherApi.getCoors("https://maps.googleapis.com/maps/api/geocode/json?address="
                + district + Constants.COMMA+province + "&key=" + "AIzaSyAqdRuDwUbXJTQ1WwdIIR6_F3k3etpb5Og")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GeocodingResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GeocodingResponse geocodingResponse) {
                        listener.onSuccess((new LatLng(geocodingResponse.getResultsResponseList().get(0).getGeometryResponse().getLocation().getLat(),
                                            geocodingResponse.getResultsResponseList().get(0).getGeometryResponse().getLocation().getLon())));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static PlaceMarkList getPlaceMarkList(Context context) {
        InputStream in_s = context.getResources().openRawResource(R.raw.diaphanhuyen);
        PlaceMarkList placeMarkList = new PlaceMarkList();
        List<PlaceMark> mPlaceMarkList = new ArrayList<>();
        Map<String, Map<String, List<LatLng>>> mLocation = new LinkedHashMap<>();
        List<String> disList = new ArrayList<>();
        Document doc = null;
        try {
            doc = Jsoup.parse(in_s, "UTF-8", "");
            for(Element e : doc.select("Placemark")) {
                PlaceMark placeMark = new PlaceMark();
                // the contents
                Elements es =  e.select("SimpleData");
                placeMark.setProvince(es.get(2).text());
                placeMark.setDistrict(es.get(3).text());
                if (mLocation.size() == 0 || !mLocation.containsKey(placeMark.getProvince())) {
                    if (mLocation.size() == 0) {
                        Map<String, List<LatLng>> dis = new LinkedHashMap<>();
                        dis.put("HUYỆN", new ArrayList<>());
                        mLocation.put("TỈNH", dis);
                    }
                    mLocation.put(placeMark.getProvince(), new LinkedHashMap<>());
                }

                    Map<String, List<LatLng>> dis = mLocation.get(placeMark.getProvince());
                    if (dis.size() == 0){
                        dis.put("HUYỆN", new ArrayList<>());
                        dis.put("TẤT CẢ", new ArrayList<>());
                    }

                    disList.add(placeMark.getDistrict());
                    placeMark.setPopulation(Integer.parseInt(es.get(4).text()));
                    String[] coors = e.select("coordinates").text().split(" ");
                    List<LatLng> latLngs = new ArrayList<>();
                    for (String coor : coors) {
                        String[] temp = coor.split(",");
                        latLngs.add(new LatLng(Double.parseDouble(temp[1]),
                                Double.parseDouble(temp[0])));
                    }
                    placeMark.setLatLngs(latLngs);
                    mPlaceMarkList.add(placeMark);
                    dis.put(placeMark.getDistrict(), latLngs);//dis - lat
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(mLocation.toString());
        placeMarkList.setPlaceMarkList(mPlaceMarkList);
        placeMarkList.setLocation(mLocation);
            return placeMarkList;
    }

    public static void fetchingWeather(final Context context, WeatherApi weatherApi, String lat, String lon, String address,
                                 OnGetData<WeatherForecastResponse> listener) {
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_app_widget);
        final ComponentName watchWidget = new ComponentName(context, WeatherAppWidget.class);
        Toast.makeText(context, "Requested", Toast.LENGTH_SHORT).show();
            weatherApi = UtilsApi.getAPIService();
            weatherApi.requestRepos(lat,
                    lon,
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
                           listener.onSuccess(weatherForecastResponses);

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
