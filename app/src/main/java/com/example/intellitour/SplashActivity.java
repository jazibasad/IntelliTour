package com.example.intellitour;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo_image);
        TextView text = findViewById(R.id.app_name_text);

        // Ensure views are visible but transparent
        logo.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
        logo.setAlpha(0f);
        text.setAlpha(0f);

        // Animate Logo: Fade in over 1 second
        logo.animate()
                .alpha(1f)
                .setDuration(1000)
                .start();

        // Animate Text: Fade in over 1 second, starting after 1 second
        text.animate()
                .alpha(1f)
                .setStartDelay(1000)
                .setDuration(1000)
                .start();

        // Navigate to LoginActivity after 3 seconds total
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}