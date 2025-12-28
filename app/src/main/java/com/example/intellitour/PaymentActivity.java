package com.example.intellitour;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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
            showPaymentDialog("Easypaisa", "03001234567", packagePrice, packageName);
        });

        cardBank.setOnClickListener(v -> {
            showPaymentDialog("Bank Transfer", "PK12 ABCD 0000 1234 5678 9012", packagePrice, packageName);
        });
    }

    private void showPaymentDialog(String method, String details, String requiredAmount, String packageName) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_payment_input, null);
        TextView tvPaymentDetails = dialogView.findViewById(R.id.tv_payment_details);
        TextInputEditText etAmount = dialogView.findViewById(R.id.et_payment_amount);

        String detailsText = "Please transfer " + requiredAmount + " to the following " + method + " account:\n\nAccount: " + details;
        tvPaymentDetails.setText(detailsText);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter Payment Details")
                .setView(dialogView)
                .setPositiveButton("Confirm Payment", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String enteredAmount = etAmount.getText().toString().trim();
                String cleanRequiredAmount = requiredAmount.replaceAll("[^\\d]", "");

                if (enteredAmount.isEmpty()) {
                    Toast.makeText(this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (enteredAmount.equals(cleanRequiredAmount)) {
                    dialog.dismiss();
                    saveBookingToFirestore(packageName, requiredAmount, method);
                } else {
                    Toast.makeText(this, "Incorrect amount. Please enter exactly " + requiredAmount, Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
    }

    private void saveBookingToFirestore(String packageName, String packagePrice, String paymentMethod) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to make a booking.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        Map<String, Object> booking = new HashMap<>();
        booking.put("userId", userId);
        booking.put("packageName", packageName);
        booking.put("packagePrice", packagePrice);
        booking.put("paymentMethod", paymentMethod);
        booking.put("bookingDate", FieldValue.serverTimestamp());

        db.collection("bookings").add(booking)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Booking saved with ID: " + documentReference.getId());
                    showSuccessDialog();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding booking", e);
                    Toast.makeText(PaymentActivity.this, "Booking failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Payment Confirmed!")
                .setMessage("Your booking has been successfully confirmed.")
                .setPositiveButton("OK", (dialog, which) -> {
                    finishAffinity();
                    startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                })
                .setCancelable(false)
                .show();
    }
}