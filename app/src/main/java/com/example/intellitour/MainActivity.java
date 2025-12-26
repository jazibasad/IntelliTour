package com.example.intellitour;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        View cardAiPlan = findViewById(R.id.card_ai_plan);
        View cardMap = findViewById(R.id.card_map);
        View cardWeather = findViewById(R.id.card_weather);
        View cardPackages = findViewById(R.id.card_packages);
        ImageButton btnLogout = findViewById(R.id.btn_logout);

        // Set listeners
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
        
        // Handle Logout
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("isLoggedIn", false).apply();
            
            // Navigate back to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}