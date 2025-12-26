package com.example.intellitour;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View logo = findViewById(R.id.logo_image);
        View text = findViewById(R.id.app_name_text);

        // Fade in logo
        Animation fadeInLogo = new AlphaAnimation(0f, 1f);
        fadeInLogo.setDuration(1000); // 1 second
        fadeInLogo.setFillAfter(true);
        logo.startAnimation(fadeInLogo);

        // Fade in text after a delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Animation fadeInText = new AlphaAnimation(0f, 1f);
            fadeInText.setDuration(1000); // 1 second
            fadeInText.setFillAfter(true);
            text.startAnimation(fadeInText);
        }, 1000); // Start text animation after 1 second

        // Navigate to LoginActivity after total delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000); // 3 seconds total delay
    }
}