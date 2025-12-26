package com.example.intellitour;

import android.content.Intent;
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
        Button btnNextPage = findViewById(R.id.btn_next_page);

        btnBookHunza.setOnClickListener(v -> showBookingDialog("Hunza Valley Exploration", "Rs. 75,000"));
        btnBookSkardu.setOnClickListener(v -> showBookingDialog("Skardu & Deosai Plains", "Rs. 95,000"));
        btnBookNaran.setOnClickListener(v -> showBookingDialog("Naran & Kaghan Valley Tour", "Rs. 50,000"));
        btnBookGilgit.setOnClickListener(v -> showBookingDialog("Gilgit Adventure", "Rs. 60,000"));
        btnBookKashmir.setOnClickListener(v -> showBookingDialog("Kashmir Neelum Valley", "Rs. 55,000"));

        btnNextPage.setOnClickListener(v -> {
            Intent intent = new Intent(PackagesActivity.this, PackagesActivity2.class);
            startActivity(intent);
        });
    }

    private void showBookingDialog(String packageName, String price) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage("Do you want to proceed to payment for the " + packageName + " package?")
                .setPositiveButton("Proceed", (dialog, which) -> {
                    Intent intent = new Intent(PackagesActivity.this, PaymentActivity.class);
                    intent.putExtra("PACKAGE_NAME", packageName);
                    intent.putExtra("PACKAGE_PRICE", price);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}