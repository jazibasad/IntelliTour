package com.example.intellitour;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "WeatherActivity";
    private static final String API_KEY = "f6c447ffc4b7b5f1961f90f21aa97d2f"; // Remember to add your key
    private static final String BASE_URL = "https://api.openweathermap.org/";

    private ForecastAdapter forecastAdapter;
    private List<ForecastResponse.ForecastItem> forecastItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        EditText etCity = findViewById(R.id.et_city);
        Button btnGetWeather = findViewById(R.id.btn_get_weather);
        TextView tvPlaceholder = findViewById(R.id.tv_weather_placeholder);
        LinearLayout resultsLayout = findViewById(R.id.weather_results_layout);
        RecyclerView rvForecast = findViewById(R.id.rv_forecast);

        // Setup RecyclerView
        rvForecast.setLayoutManager(new LinearLayoutManager(this));
        forecastAdapter = new ForecastAdapter(forecastItems);
        rvForecast.setAdapter(forecastAdapter);

        btnGetWeather.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (API_KEY.equals("PASTE_YOUR_OWN_API_KEY_HERE")) {
                resultsLayout.setVisibility(View.GONE);
                tvPlaceholder.setVisibility(View.VISIBLE);
                tvPlaceholder.setText("API Key is missing in WeatherActivity.java");
                return;
            }

            // Hide previous results and show loading text
            resultsLayout.setVisibility(View.GONE);
            tvPlaceholder.setVisibility(View.VISIBLE);
            tvPlaceholder.setText("Loading...");

            fetchCurrentWeather(city, resultsLayout);
            fetchForecast(city, resultsLayout);
        });
    }

    private void fetchCurrentWeather(String city, LinearLayout resultsLayout) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getWeather(city, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultsLayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.tv_weather_placeholder).setVisibility(View.GONE);
                    populateCurrentWeather(response.body());
                } 
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                 // Failure is handled by the forecast call
            }
        });
    }

    private void fetchForecast(String city, LinearLayout resultsLayout) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<ForecastResponse> call = service.getForecast(city, API_KEY, "metric");

        call.enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    forecastItems.clear();
                    for (int i = 0; i < response.body().list.size(); i += 8) {
                        forecastItems.add(response.body().list.get(i));
                    }
                    forecastAdapter.notifyDataSetChanged();
                } else {
                    resultsLayout.setVisibility(View.GONE);
                    TextView tvPlaceholder = findViewById(R.id.tv_weather_placeholder);
                    tvPlaceholder.setVisibility(View.VISIBLE);
                    if (response.code() == 404) {
                        tvPlaceholder.setText("Error: City '" + city + "' not found.");
                    } else {
                        tvPlaceholder.setText("Error fetching forecast: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                resultsLayout.setVisibility(View.GONE);
                TextView tvPlaceholder = findViewById(R.id.tv_weather_placeholder);
                tvPlaceholder.setVisibility(View.VISIBLE);
                tvPlaceholder.setText("Network Error. Please check connection.");
            }
        });
    }

    private void populateCurrentWeather(WeatherResponse weather) {
        MaterialCardView card = findViewById(R.id.current_weather_card);
        TextView tvCityName = card.findViewById(R.id.tv_city_name);
        ImageView ivWeatherIcon = card.findViewById(R.id.iv_weather_icon);
        TextView tvTemperature = card.findViewById(R.id.tv_temperature);
        TextView tvCondition = card.findViewById(R.id.tv_condition);
        TextView tvHumidity = card.findViewById(R.id.tv_humidity);

        // This check is important because this card might not exist in all layouts
        if(tvCityName != null) {
            tvCityName.setText(weather.name);
            tvTemperature.setText(String.format("%.0f°C", weather.main.temp));
            tvCondition.setText(weather.weather.get(0).mainCondition);
            tvHumidity.setText("Humidity: " + weather.main.humidity + "%");

            String condition = weather.weather.get(0).mainCondition.toLowerCase();
            if (condition.contains("clear")) {
                ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_day);
            } else if (condition.contains("cloud")) {
                ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_day);
            } else {
                ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_help);
            }
        }
    }
}