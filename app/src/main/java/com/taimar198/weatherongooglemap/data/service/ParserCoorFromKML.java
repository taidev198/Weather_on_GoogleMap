package com.taimar198.weatherongooglemap.data.service;

import android.content.Context;
import android.os.AsyncTask;

import com.taimar198.weatherongooglemap.data.model.PlaceMark;
import com.taimar198.weatherongooglemap.utls.Methods;

import java.util.List;

public class ParserCoorFromKML extends AsyncTask<Void, Void, List<PlaceMark>> {

    private List<PlaceMark> mPlaceMarkList;
    private OnParsingData mListener;
    private Context mContext;

    public ParserCoorFromKML(Context context, OnParsingData listener) {
        mContext = context;
        mListener = listener;
    }


    @Override
    protected List<PlaceMark> doInBackground(Void... voids) {

        return Methods.getPlaceMarkList(mContext);
    }

    @Override
    protected void onPostExecute(List<PlaceMark> result) {
        super.onPostExecute(result);

        mListener.onSuccess(result);
    }

    public interface OnParsingData {
        void onSuccess(List<PlaceMark> placeMarkList);
        void onFailure(Exception e);
    }
}
