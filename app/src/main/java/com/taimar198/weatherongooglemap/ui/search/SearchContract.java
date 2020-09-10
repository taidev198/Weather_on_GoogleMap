package com.taimar198.weatherongooglemap.ui.search;

import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.ui.base.BasePresenter;
import com.taimar198.weatherongooglemap.ui.base.BaseView;

public interface SearchContract {
    interface View extends BaseView<Presenter> {
        void showIntroSearch();

        void hideIntroSearch();

        void showProgressBar();

        void hideProgressBar();

        void showSearchResult(CurrentWeather currentWeather);

        void showError(String errMsg);

        void showSuccess(String msg);

    }

    interface Presenter extends BasePresenter {

        void searchCityNameResult(String cityName);

        void saveRecentSearch(String query);

        void getCurrentWeather(String query);

    }
}
