package com.example.intellitour;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo_image);
        TextView appName = findViewById(R.id.app_name_text);
        TextView appTagline = findViewById(R.id.app_tagline_text);

        // Set initial state for animations
        logo.setVisibility(View.VISIBLE);
        logo.setAlpha(0f);
        logo.setScaleX(0.8f);
        logo.setScaleY(0.8f);

        appName.setVisibility(View.VISIBLE);
        appName.setAlpha(0f);
        appName.setTranslationY(50f);
        
        appTagline.setVisibility(View.VISIBLE);
        appTagline.setAlpha(0f);

        // Start animations
        // Logo animation: Scale up and fade in
        logo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(1000)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        // App Name animation: Slide up and fade in
        appName.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .setStartDelay(500) // Start after logo animation begins
                .setInterpolator(new DecelerateInterpolator())
                .start();

        // Tagline animation: Fade in
        appTagline.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(1000) // Start after app name animation begins
                .start();

        // Navigate to next activity after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            Intent intent = isLoggedIn ? new Intent(SplashActivity.this, MainActivity.class) :
                                         new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // Total splash screen duration
    }
}