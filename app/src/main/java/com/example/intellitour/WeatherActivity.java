package com.example.intellitour;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        EditText etCity = findViewById(R.id.et_city);
        Button btnGetWeather = findViewById(R.id.btn_get_weather);
        TextView tvResult = findViewById(R.id.tv_weather_result);

        btnGetWeather.setOnClickListener(v -> {
            String city = etCity.getText().toString();
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mock Weather Data
            // In a real app, you would use Retrofit to call OpenWeatherMap API here.
            String weatherInfo = "Current Weather in " + city + ":\n" +
                    "Temperature: 25°C\n" +
                    "Condition: Sunny\n" +
                    "Humidity: 40%";
            
            tvResult.setText(weatherInfo);
        });
    }
}