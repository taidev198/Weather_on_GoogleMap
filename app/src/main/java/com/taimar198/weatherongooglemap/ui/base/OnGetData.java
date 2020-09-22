package com.taimar198.weatherongooglemap.ui.base;

public interface OnGetData<T> {

    void onSuccess(T t);
    void onFailure(Exception e);

}
