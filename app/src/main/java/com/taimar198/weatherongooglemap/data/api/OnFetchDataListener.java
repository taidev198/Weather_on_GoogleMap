package com.taimar198.weatherongooglemap.data.api;

import android.graphics.Bitmap;

public interface OnFetchDataListener<T> {

     void onFetchDataSuccess(Bitmap data);

     void onFetchDataFailure(Exception e);
}
