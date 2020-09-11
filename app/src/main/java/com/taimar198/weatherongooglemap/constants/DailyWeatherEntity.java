package com.taimar198.weatherongooglemap.constants;

import androidx.annotation.StringDef;

@StringDef({
        DailyWeatherEntity.DAILY_OBJECT,
        DailyWeatherEntity.ARRAY_DATA_DAILY,
        DailyWeatherEntity.TIME,
        DailyWeatherEntity.WEATHER,
        DailyWeatherEntity.ICON,
        DailyWeatherEntity.TEMP_MAX,
        DailyWeatherEntity.TEMP_MIN
})
public @interface DailyWeatherEntity {
    String DAILY_OBJECT = "daily";
    String ARRAY_DATA_DAILY = "data";
    String TIME = "time";
    String WEATHER = "summary";
    String ICON = "icon";
    String TEMP_MAX = "temperatureHigh";
    String TEMP_MIN = "temperatureLow";
}