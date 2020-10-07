package com.taimar198.weatherongooglemap.data.local;

public interface OnDataLoadingCallback<T> {

    void onDataLoaded(T data);

    void onDataNotAvailable(Exception e);

}