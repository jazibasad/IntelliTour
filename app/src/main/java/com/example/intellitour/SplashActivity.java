package com.example.intellitour;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "IntelliTourChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        createNotificationChannel();

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
        logo.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
        appName.animate().alpha(1f).translationY(0f).setDuration(1000).setStartDelay(500).setInterpolator(new DecelerateInterpolator()).start();
        appTagline.animate().alpha(1f).setDuration(1000).setStartDelay(1000).start();

        // Navigate to LoginActivity after delay. LoginActivity will handle the auth check.
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "IntelliTour Channel";
            String description = "Channel for IntelliTour notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}