package com.taimar198.weatherongooglemap.constants;

import androidx.annotation.StringDef;

@StringDef({
        HourlyEntity.HOURLY_OBJECT,
        HourlyEntity.ARRAY_DATA_HOURLY,
        HourlyEntity.TIME,
        HourlyEntity.TEMP,
        HourlyEntity.WEATHER,
        HourlyEntity.ICON
})
public @interface HourlyEntity {
    String HOURLY_OBJECT = "hourly";
    String ARRAY_DATA_HOURLY = "data";
    String TIME = "time";
    String TEMP = "temperature";
    String WEATHER = "summary";
    String ICON = "icon";
}