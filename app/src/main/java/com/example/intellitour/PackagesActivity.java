package com.example.intellitour;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PackagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        // In a real app, you would set OnClickListeners for the "Book Now" buttons here
        // to navigate to a booking screen or process a payment.
        
        // Example for one button (assuming you give IDs to buttons in XML later for logic):
        // Button btnBookParis = findViewById(R.id.btn_book_paris);
        // btnBookParis.setOnClickListener(v -> Toast.makeText(this, "Booking feature coming soon!", Toast.LENGTH_SHORT).show());
    }
}