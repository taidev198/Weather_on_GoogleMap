package com.taimar198.weatherongooglemap.data.model;

import java.util.List;
import java.util.Map;

public class PlaceMarkList {

    private List<PlaceMark> mPlaceMarkList;
    private Map<String, List<String>> mLocation;

    public PlaceMarkList() {

    }

    public List<PlaceMark> getPlaceMarkList() {
        return mPlaceMarkList;
    }

    public void setPlaceMarkList(List<PlaceMark> mPlaceMarkList) {
        this.mPlaceMarkList = mPlaceMarkList;
    }

    public Map<String, List<String>> getLocation() {
        return mLocation;
    }

    public void setLocation(Map<String, List<String>> mLocation) {
        this.mLocation = mLocation;
    }
}
