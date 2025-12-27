package com.example.intellitour;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        logo.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
        appName.animate().alpha(1f).translationY(0f).setDuration(1000).setStartDelay(500).setInterpolator(new DecelerateInterpolator()).start();
        appTagline.animate().alpha(1f).setDuration(1000).setStartDelay(1000).start();

        // Navigate to next activity after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent;
            if (currentUser != null) {
                // User is signed in, go to MainActivity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // No user is signed in, go to LoginActivity
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 3000); // Total splash screen duration
    }
}