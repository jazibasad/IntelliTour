package com.example.intellitour;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

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

    private void showPaymentDialog(String method, String details, String requiredAmount) {
        // Inflate the custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_payment_input, null);
        TextView tvPaymentDetails = dialogView.findViewById(R.id.tv_payment_details);
        TextInputEditText etAmount = dialogView.findViewById(R.id.et_payment_amount);

        // Set the details in the dialog
        String detailsText = "Please transfer " + requiredAmount + " to the following " + method + " account:\n\nAccount: " + details;
        tvPaymentDetails.setText(detailsText);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter Payment Details")
                .setView(dialogView)
                .setPositiveButton("Confirm Payment", null) // Set to null to override and prevent auto-closing
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String enteredAmount = etAmount.getText().toString().trim();
                String cleanRequiredAmount = requiredAmount.replaceAll("[^\\d]", ""); // Remove "Rs. " and commas

                if (enteredAmount.isEmpty()) {
                    Toast.makeText(this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (enteredAmount.equals(cleanRequiredAmount)) {
                    dialog.dismiss();
                    showSuccessDialog();
                } else {
                    Toast.makeText(this, "Incorrect amount. Please enter exactly " + requiredAmount, Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
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