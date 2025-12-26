package com.example.intellitour;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get data from Intent
        String packageName = getIntent().getStringExtra("PACKAGE_NAME");
        String packagePrice = getIntent().getStringExtra("PACKAGE_PRICE");

        // Find views
        TextView tvPackageName = findViewById(R.id.tv_package_name);
        TextView tvPackagePrice = findViewById(R.id.tv_package_price);
        MaterialCardView cardEasypaisa = findViewById(R.id.card_easypaisa);
        MaterialCardView cardBank = findViewById(R.id.card_bank);

        // Set data
        tvPackageName.setText(packageName);
        tvPackagePrice.setText(packagePrice);

        // Set listeners
        cardEasypaisa.setOnClickListener(v -> {
            showPaymentDialog("Easypaisa", "03001234567", packagePrice);
        });

        cardBank.setOnClickListener(v -> {
            showPaymentDialog("Bank Transfer", "PK12 ABCD 0000 1234 5678 9012", packagePrice);
        });
    }

    private void showPaymentDialog(String method, String details, String amount) {
        String message = "Please transfer " + amount + " to the following " + method + " account and press 'Done'.\n\n" +
                         "Account: " + details + "\n\n" +
                         "(This is a simulation. No real payment will be made.)";

        new AlertDialog.Builder(this)
                .setTitle(method + " Details")
                .setMessage(message)
                .setPositiveButton("Done", (dialog, which) -> {
                    showSuccessDialog();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Payment Confirmed!")
                .setMessage("Your booking is complete. You will receive a confirmation email shortly.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Navigate back to the main screen
                    finishAffinity();
                    startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                })
                .setCancelable(false)
                .show();
    }
}