package com.taimar198.weatherongooglemap.utls;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.LatLng;
import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.api.UtilsApi;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.model.PlaceMark;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    public static List<PlaceMark> getPlaceMarkList(Context context) {
        InputStream in_s = context.getResources().openRawResource(R.raw.diaphanhuyen);
        List<PlaceMark> mPlaceMarkList = new ArrayList<>();
        Document doc = null;
        try {
            PlaceMark placeMark = new PlaceMark();
            doc = Jsoup.parse(in_s, "UTF-8", "");
            for(Element e : doc.select("Placemark")) {
                // the contents
                Elements es =  e.select("SimpleData");
                placeMark.setProvince(es.get(2).text());
                placeMark.setDistrict(es.get(3).text());
                placeMark.setPopulation(Integer.parseInt(es.get(4).text()));
                String [] coors =  e.select("coordinates").text().split(" ");
                List<LatLng> latLngs = new ArrayList<>();
                for (int i = 0; i< coors.length; i++) {
                    String[] temp = coors[i].split(",");
                    latLngs.add(new LatLng(Double.parseDouble(temp[1]),
                            Double.parseDouble(temp[0])));
                    System.out.println(latLngs.get(i).toString());
                }
                placeMark.setLatLngs(latLngs);
                mPlaceMarkList.add(placeMark);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            return mPlaceMarkList;
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
