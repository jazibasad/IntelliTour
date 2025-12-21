package com.example.intellitour;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // In the new layout, we are using Cards instead of Buttons. 
        // We find them by ID and set OnClickListeners.

        View cardAiPlan = findViewById(R.id.card_ai_plan);
        View cardMap = findViewById(R.id.card_map);
        View cardWeather = findViewById(R.id.card_weather);
        View cardPackages = findViewById(R.id.card_packages);

        cardAiPlan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AiPlannerActivity.class);
            startActivity(intent);
        });

        cardMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });

        cardWeather.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
        });

        cardPackages.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PackagesActivity.class);
            startActivity(intent);
        });
    }
}