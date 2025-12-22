package com.example.intellitour;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    // TODO: Replace with your actual OpenWeatherMap API Key
    private static final String API_KEY = "f6c447ffc4b7b5f1961f90f21aa97d2f";
    private static final String BASE_URL = "https://api.openweathermap.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        EditText etCity = findViewById(R.id.et_city);
        Button btnGetWeather = findViewById(R.id.btn_get_weather);
        TextView tvResult = findViewById(R.id.tv_weather_result);

        btnGetWeather.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }

            fetchWeatherData(city, tvResult);
        });
    }

    private void fetchWeatherData(String city, TextView tvResult) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getWeather(city, API_KEY, "metric"); // metric for Celsius

        tvResult.setText("Loading...");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    String temp = String.format("%.1f", weather.main.temp);
                    String condition = weather.weather.get(0).mainCondition;
                    String description = weather.weather.get(0).description;
                    String humidity = String.valueOf(weather.main.humidity);

                    String resultText = "Current Weather in " + weather.name + ":\n\n" +
                            "Temperature: " + temp + "°C\n" +
                            "Condition: " + condition + " (" + description + ")\n" +
                            "Humidity: " + humidity + "%";

                    tvResult.setText(resultText);
                } else {
                    tvResult.setText("Error: City not found or API issue.");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                tvResult.setText("Network Error: " + t.getMessage());
            }
        });
    }
}