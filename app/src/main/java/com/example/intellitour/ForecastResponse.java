package com.example.intellitour;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {

    @SerializedName("list")
    public List<ForecastItem> list;

    // This class represents a single forecast data point in the list
    public static class ForecastItem {
        @SerializedName("dt_txt")
        public String dateTimeText;

        @SerializedName("main")
        public WeatherResponse.Main main;

        @SerializedName("weather")
        public List<WeatherResponse.Weather> weather;
    }
}