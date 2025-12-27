package com.example.intellitour;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PackagesActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages_page2);

        // Booking Buttons
        Button btnBookSwat = findViewById(R.id.btn_book_swat);
        Button btnBookFairy = findViewById(R.id.btn_book_fairy);
        Button btnBookChitral = findViewById(R.id.btn_book_chitral);
        Button btnBookMurree = findViewById(R.id.btn_book_murree);
        Button btnBookGwadar = findViewById(R.id.btn_book_gwadar);
        Button btnPrevPage = findViewById(R.id.btn_prev_page);

        // Wikipedia Link TextViews
        TextView wikiLinkSwat = findViewById(R.id.wiki_link_swat);
        TextView wikiLinkFairy = findViewById(R.id.wiki_link_fairy);
        TextView wikiLinkChitral = findViewById(R.id.wiki_link_chitral);
        TextView wikiLinkMurree = findViewById(R.id.wiki_link_murree);
        TextView wikiLinkGwadar = findViewById(R.id.wiki_link_gwadar);

        // Set OnClick Listeners for Booking
        btnBookSwat.setOnClickListener(v -> showBookingDialog("Swat Valley (Switzerland of East)", "Rs. 45,000"));
        btnBookFairy.setOnClickListener(v -> showBookingDialog("Fairy Meadows Trek", "Rs. 50,000"));
        btnBookChitral.setOnClickListener(v -> showBookingDialog("Chitral & Kalash Valley", "Rs. 65,000"));
        btnBookMurree.setOnClickListener(v -> showBookingDialog("Murree & Galiyat", "Rs. 25,000"));
        btnBookGwadar.setOnClickListener(v -> showBookingDialog("Gwadar Beach & Ormara", "Rs. 40,000"));

        // Set OnClick Listeners for Wikipedia Links
        wikiLinkSwat.setOnClickListener(v -> openLink("https://en.wikipedia.org/wiki/Swat_District"));
        wikiLinkFairy.setOnClickListener(v -> openLink("https://en.wikipedia.org/wiki/Fairy_Meadows"));
        wikiLinkChitral.setOnClickListener(v -> openLink("https://en.wikipedia.org/wiki/Kalash_Valleys"));
        wikiLinkMurree.setOnClickListener(v -> openLink("https://en.wikipedia.org/wiki/Murree"));
        wikiLinkGwadar.setOnClickListener(v -> openLink("https://en.wikipedia.org/wiki/Gwadar"));

        btnPrevPage.setOnClickListener(v -> finish());
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

    private void openLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}