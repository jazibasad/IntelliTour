package com.example.intellitour;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PackagesActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages_page2);

        Button btnBookSwat = findViewById(R.id.btn_book_swat);
        Button btnBookFairy = findViewById(R.id.btn_book_fairy);
        Button btnBookChitral = findViewById(R.id.btn_book_chitral);
        Button btnBookMurree = findViewById(R.id.btn_book_murree);
        Button btnBookGwadar = findViewById(R.id.btn_book_gwadar);
        Button btnPrevPage = findViewById(R.id.btn_prev_page);

        btnBookSwat.setOnClickListener(v -> showBookingDialog("Swat Valley (Switzerland of East)", "Rs. 45,000"));
        btnBookFairy.setOnClickListener(v -> showBookingDialog("Fairy Meadows Trek", "Rs. 50,000"));
        btnBookChitral.setOnClickListener(v -> showBookingDialog("Chitral & Kalash Valley", "Rs. 65,000"));
        btnBookMurree.setOnClickListener(v -> showBookingDialog("Murree & Galiyat", "Rs. 25,000"));
        btnBookGwadar.setOnClickListener(v -> showBookingDialog("Gwadar Beach & Ormara", "Rs. 40,000"));

        btnPrevPage.setOnClickListener(v -> {
            finish(); // Simply close this activity to go back to the first page
        });
    }

    private void showBookingDialog(String packageName, String price) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Booking")
                .setMessage("Do you want to proceed to payment for the " + packageName + " package?")
                .setPositiveButton("Proceed", (dialog, which) -> {
                    Intent intent = new Intent(PackagesActivity2.this, PaymentActivity.class);
                    intent.putExtra("PACKAGE_NAME", packageName);
                    intent.putExtra("PACKAGE_PRICE", price);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}