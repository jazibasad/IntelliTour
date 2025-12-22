package com.example.intellitour;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    @SerializedName("main")
    public Main main;

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("name")
    public String name;

    public static class Main {
        @SerializedName("temp")
        public float temp;

        @SerializedName("humidity")
        public float humidity;
    }

    public static class Weather {
        @SerializedName("description")
        public String description;
        
        @SerializedName("main")
        public String mainCondition;
    }
}