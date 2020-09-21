package com.taimar198.weatherongooglemap.data.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.taimar198.weatherongooglemap.data.model.Location;

public class GeometryResponse {

    @Expose
    @SerializedName("location")
    private LocationResponse location;

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }
}
