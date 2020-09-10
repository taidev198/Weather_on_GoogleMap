package com.taimar198.weatherongooglemap.ui.search;

import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.data.repository.CurrentWeatherRepository;
import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;

public class SearchPresenter implements SearchContract.Presenter,
        CurrentWeatherDataSource.OnFetchDataListener {

    private SearchContract.View mView;
    private CurrentWeatherRepository mCurrentWeatherRepository;

    public SearchPresenter(SearchContract.View view, CurrentWeatherRepository repository){
        mCurrentWeatherRepository = repository;
        mView = view;
    }

    @Override
    public void searchCityNameResult(String cityName) {
        mCurrentWeatherRepository.getCurrentWeatherByCityName(this, cityName);
    }

    @Override
    public void saveRecentSearch(String query) {

    }

    @Override
    public void getCurrentWeather(String query) {

    }

    @Override
    public void start() {

    }

    @Override
    public void onFetchDataSuccess(CurrentWeather data) {
        System.out.println(data.toString());
    }

    @Override
    public void onFetchDataFailure(Exception e) {

    }
}
