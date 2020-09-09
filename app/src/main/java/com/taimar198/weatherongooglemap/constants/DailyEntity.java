package com.taimar198.weatherongooglemap.constants;

import androidx.annotation.StringDef;

@StringDef({
        DailyEntity.DAILY_OBJECT,
        DailyEntity.ARRAY_DATA_DAILY,
        DailyEntity.TIME,
        DailyEntity.WEATHER,
        DailyEntity.ICON,
        DailyEntity.TEMP_MAX,
        DailyEntity.TEMP_MIN
})
public @interface DailyEntity {
    String DAILY_OBJECT = "daily";
    String ARRAY_DATA_DAILY = "data";
    String TIME = "time";
    String WEATHER = "summary";
    String ICON = "icon";
    String TEMP_MAX = "temperatureHigh";
    String TEMP_MIN = "temperatureLow";
}