package com.taimar198.weatherongooglemap.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.data.model.WeatherForecast;

public class CardAdapter  extends FragmentPagerAdapter {

    private WeatherForecastResponse mCurrentWeather;

    public CardAdapter(@NonNull FragmentManager fm, WeatherForecastResponse currentWeather, int behavior) {
        super(fm, behavior);
        mCurrentWeather = currentWeather;
    }
    @Override
    public int getItemPosition(Object object) {
// POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

//        switch (position) {
//            case 1:mCurrentWeather.getCurrentWeather().getLocation().setCity(String.valueOf(1));
//                return  ScreenSlidePageFragment.newInstance(mCurrentWeather);
//
//            case 2: mCurrentWeather.getCurrentWeather().getLocation().setCity(String.valueOf(2));
//                return  ScreenSlidePageFragment.newInstance(mCurrentWeather);
//        }
     //   mCurrentWeather.getCurrentWeather().getLocation().setCity(String.valueOf(3));
        System.out.println(mCurrentWeather.getCurrentWeather().getWeathers().get(0).getDescription());
        return  ScreenSlidePageFragment.newInstance(mCurrentWeather);
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void setData(WeatherForecastResponse currentWeather) {
        mCurrentWeather = currentWeather;
    }
}
