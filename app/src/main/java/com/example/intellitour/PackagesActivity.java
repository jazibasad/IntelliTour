package com.example.intellitour;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PackagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        Button btnBookHunza = findViewById(R.id.btn_book_hunza);
        Button btnBookSkardu = findViewById(R.id.btn_book_skardu);
        Button btnBookNaran = findViewById(R.id.btn_book_naran);
        Button btnBookGilgit = findViewById(R.id.btn_book_gilgit);
        Button btnBookKashmir = findViewById(R.id.btn_book_kashmir);

        btnBookHunza.setOnClickListener(v -> showBookingDialog("Hunza Valley Exploration", "Rs. 75,000"));
        btnBookSkardu.setOnClickListener(v -> showBookingDialog("Skardu & Deosai Plains", "Rs. 95,000"));
        btnBookNaran.setOnClickListener(v -> showBookingDialog("Naran & Kaghan Valley Tour", "Rs. 50,000"));
        btnBookGilgit.setOnClickListener(v -> showBookingDialog("Gilgit Adventure", "Rs. 60,000"));
        btnBookKashmir.setOnClickListener(v -> showBookingDialog("Kashmir Neelum Valley", "Rs. 55,000"));
    }

    private void showBookingDialog(String packageName, String price) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage("Do you want to book the " + packageName + " package for " + price + "?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // Logic for booking confirmation (e.g., save to Firebase)
                    showSuccessDialog();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Booking Successful!")
                .setMessage("Your trip has been booked successfully. Our team will contact you shortly.")
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}