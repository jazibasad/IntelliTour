package com.example.intellitour;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    // TODO: IMPORTANT! Replace this with your own free API key from openweathermap.org
    private static final String API_KEY = "f6c447ffc4b7b5f1961f90f21aa97d2f";
    private static final String BASE_URL = "https://api.openweathermap.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        EditText etCity = findViewById(R.id.et_city);
        Button btnGetWeather = findViewById(R.id.btn_get_weather);
        MaterialCardView resultCard = findViewById(R.id.weather_card_result);
        TextView tvPlaceholder = findViewById(R.id.tv_weather_placeholder);

        btnGetWeather.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (API_KEY.equals("PASTE_YOUR_OWN_API_KEY_HERE")) {
                tvPlaceholder.setText("API Key is missing. Please add your key in WeatherActivity.java.");
                return;
            }

            fetchWeatherData(city, resultCard, tvPlaceholder);
        });
    }

    private void fetchWeatherData(String city, MaterialCardView resultCard, TextView tvPlaceholder) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getWeather(city, API_KEY, "metric");

        tvPlaceholder.setVisibility(View.VISIBLE);
        tvPlaceholder.setText("Loading...");
        resultCard.setVisibility(View.GONE);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvPlaceholder.setVisibility(View.GONE);
                    resultCard.setVisibility(View.VISIBLE);

                    TextView tvCityName = findViewById(R.id.tv_city_name);
                    ImageView ivWeatherIcon = findViewById(R.id.iv_weather_icon);
                    TextView tvTemperature = findViewById(R.id.tv_temperature);
                    TextView tvCondition = findViewById(R.id.tv_condition);
                    TextView tvHumidity = findViewById(R.id.tv_humidity);

                    WeatherResponse weather = response.body();
                    tvCityName.setText(weather.name);
                    tvTemperature.setText(String.format("%.0f°C", weather.main.temp));
                    tvCondition.setText(weather.weather.get(0).mainCondition);
                    tvHumidity.setText("Humidity: " + weather.main.humidity + "%");

                    String condition = weather.weather.get(0).mainCondition.toLowerCase();
                    if (condition.contains("clear")) {
                        ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_day);
                    } else if (condition.contains("cloud")) {
                        ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_day);
                    } else if (condition.contains("rain")) {
                        ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_day);
                    } else {
                        ivWeatherIcon.setImageResource(android.R.drawable.ic_menu_help);
                    }

                } else {
                    resultCard.setVisibility(View.GONE);
                    tvPlaceholder.setVisibility(View.VISIBLE);
                    if (response.code() == 401) {
                        tvPlaceholder.setText("Error: Invalid API Key. Please get a new key from openweathermap.org and wait a few hours for it to activate.");
                    } else if (response.code() == 404) {
                        tvPlaceholder.setText("Error: City '" + city + "' not found.");
                    } else {
                        tvPlaceholder.setText("Error: " + response.code() + ". Please try again later.");
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                resultCard.setVisibility(View.GONE);
                tvPlaceholder.setVisibility(View.VISIBLE);
                tvPlaceholder.setText("Network Error. Please check your internet connection.");
                Log.e("WeatherActivity", "Network failure", t);
            }
        });
    }
}