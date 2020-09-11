package com.taimar198.weatherongooglemap.constants;

import androidx.annotation.StringDef;

@StringDef({
        HourlyWeatherEntity.HOURLY_OBJECT,
        HourlyWeatherEntity.ARRAY_DATA_HOURLY,
        HourlyWeatherEntity.TIME,
        HourlyWeatherEntity.TEMP,
        HourlyWeatherEntity.WEATHER,
        HourlyWeatherEntity.ICON,
        HourlyWeatherEntity.DESCRIPTION
})
public @interface HourlyWeatherEntity {
    String HOURLY_OBJECT = "hourly";
    String ARRAY_DATA_HOURLY = "hourly";
    String TIME = "time";
    String TEMP = "temp";
    String WEATHER = "weather";
    String DESCRIPTION = "description";
    String ICON = "icon";
}