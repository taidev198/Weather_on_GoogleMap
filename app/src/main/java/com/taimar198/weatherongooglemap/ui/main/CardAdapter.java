package com.taimar198.weatherongooglemap.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.data.model.WeatherForecast;

public class CardAdapter  extends FragmentPagerAdapter {

    private WeatherForecast mCurrentWeather;

    public CardAdapter(@NonNull FragmentManager fm, WeatherForecast currentWeather, int behavior) {
        super(fm, behavior);
        mCurrentWeather = currentWeather;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 1:mCurrentWeather.getCurrentWeather().getLocation().setCity(String.valueOf(1));
                return  ScreenSlidePageFragment.newInstance(mCurrentWeather);

            case 2: mCurrentWeather.getCurrentWeather().getLocation().setCity(String.valueOf(2));
                return  ScreenSlidePageFragment.newInstance(mCurrentWeather);
        }
        mCurrentWeather.getCurrentWeather().getLocation().setCity(String.valueOf(3));
        return  ScreenSlidePageFragment.newInstance(mCurrentWeather);
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void setData(WeatherForecast currentWeather) {
        mCurrentWeather = currentWeather;
    }
}
