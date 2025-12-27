package com.example.intellitour;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class AiPlannerActivity extends AppCompatActivity {

    // Define the valid packages with their details (Name -> [Min Budget, Min Days, Hotel, Wiki URL])
    private static final Map<String, Object[]> PACKAGES = new HashMap<>();

    static {
        // Destination Name -> {Price, Days, Hotel Name, Wiki URL}
        PACKAGES.put("Murree & Galiyat", new Object[]{25000, 3, "Pearl Continental, Bhurban", "https://en.wikipedia.org/wiki/Murree"});
        PACKAGES.put("Gwadar Beach & Ormara", new Object[]{40000, 4, "Zaver Pearl Continental Hotel", "https://en.wikipedia.org/wiki/Gwadar"});
        PACKAGES.put("Swat Valley (Switzerland of East)", new Object[]{45000, 5, "Swat Serena Hotel", "https://en.wikipedia.org/wiki/Swat_District"});
        PACKAGES.put("Naran & Kaghan", new Object[]{50000, 5, "PTDC Motel Naran", "https://en.wikipedia.org/wiki/Kaghan_Valley"});
        PACKAGES.put("Fairy Meadows Trek", new Object[]{50000, 5, "Fairy Meadows Cottages", "https://en.wikipedia.org/wiki/Fairy_Meadows"});
        PACKAGES.put("Kashmir Neelum Valley", new Object[]{55000, 5, "Pearl Continental, Muzaffarabad", "https://en.wikipedia.org/wiki/Neelum_District"});
        PACKAGES.put("Gilgit Adventure", new Object[]{60000, 6, "Gilgit Serena Hotel", "https://en.wikipedia.org/wiki/Gilgit"});
        PACKAGES.put("Chitral & Kalash Valley", new Object[]{65000, 6, "Hindukush Heights", "https://en.wikipedia.org/wiki/Kalash_Valleys"});
        PACKAGES.put("Hunza Valley", new Object[]{75000, 7, "Hunza Serena Inn", "https://en.wikipedia.org/wiki/Hunza_Valley"});
        PACKAGES.put("Skardu & Deosai", new Object[]{95000, 8, "Serena Shigar Fort", "https://en.wikipedia.org/wiki/Skardu"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_planner);

        EditText etBudget = findViewById(R.id.et_budget);
        EditText etDays = findViewById(R.id.et_days);
        Button btnGenerate = findViewById(R.id.btn_generate_itinerary);
        TextView tvResult = findViewById(R.id.tv_result);

        // Make the TextView clickable
        tvResult.setMovementMethod(LinkMovementMethod.getInstance());

        btnGenerate.setOnClickListener(v -> {
            String budgetInput = etBudget.getText().toString().trim();
            String daysInput = etDays.getText().toString().trim();

            if (budgetInput.isEmpty() || daysInput.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int budget = Integer.parseInt(budgetInput);
            int days = Integer.parseInt(daysInput);

            String bestPackage = suggestPackage(budget, days);

            if (bestPackage == null) {
                tvResult.setText("Sorry, no packages match your criteria.\n" +
                        "Try increasing your budget or changing duration.\n\n" +
                        "Packages start from Rs. 25,000 for 3 days (Murree).");
                return;
            }

            displayItineraryWithLink(tvResult, bestPackage, days, budget);
        });
    }

    private String suggestPackage(int budget, int days) {
        String bestMatch = null;
        int maxPriceFound = -1;

        for (Map.Entry<String, Object[]> entry : PACKAGES.entrySet()) {
            String pkgName = entry.getKey();
            int price = (int) entry.getValue()[0];
            int duration = (int) entry.getValue()[1];

            if (budget >= price && days >= duration) {
                if (price > maxPriceFound) {
                    maxPriceFound = price;
                    bestMatch = pkgName;
                }
            }
        }
        return bestMatch;
    }

    private void displayItineraryWithLink(TextView tvResult, String destination, int days, int budget) {
        Object[] packageDetails = PACKAGES.get(destination);
        String hotel = (String) packageDetails[2];
        String wikiUrl = (String) packageDetails[3];

        StringBuilder sb = new StringBuilder();
        sb.append("Suggested Destination: ").append(destination).append("\n");
        sb.append("Based on Budget: Rs. ").append(budget).append(" | Duration: ").append(days).append(" Days\n");
        sb.append("Recommended Hotel: ").append(hotel).append("\n");
        sb.append("More Info: Click here to read on Wikipedia").append("\n\n");
        sb.append("--- ITINERARY ---\n\n");

        if (destination.equals("Hunza Valley")) {
            sb.append("Day 1: Arrival in Gilgit, drive to Hunza.\n");
            sb.append("Day 2: Visit Karimabad, Baltit Fort, Altit Fort.\n");
            sb.append("Day 3: Eagle's Nest sunrise & Duikar.\n");
            sb.append("Day 4: Day trip to Attabad Lake & Passu Cones.\n");
            sb.append("Day 5: Visit Hussaini Suspension Bridge.\n");
            sb.append("Day 6: Khunjerab Pass (China Border).\n");
            sb.append("Day 7: Drive back to Gilgit for departure.");
        } else if (destination.equals("Skardu & Deosai")) {
            sb.append("Day 1: Fly to Skardu, check-in.\n");
            sb.append("Day 2: Shangrila Resort (Lower Kachura Lake).\n");
            sb.append("Day 3: Upper Kachura Lake & boating.\n");
            sb.append("Day 4: Drive to Deosai Plains, Sheosar Lake.\n");
            sb.append("Day 5: Camping at Deosai.\n");
            sb.append("Day 6: Visit Shigar Fort and Cold Desert.\n");
            sb.append("Day 7: Sadpara Lake exploration.\n");
            sb.append("Day 8: Shopping in Skardu Bazar & Departure.");
        } else if (destination.equals("Naran & Kaghan")) {
            sb.append("Day 1: Drive from Islamabad to Naran.\n");
            sb.append("Day 2: Jeep ride to Saif-ul-Malook Lake.\n");
            sb.append("Day 3: Rafting in Kunhar River.\n");
            sb.append("Day 4: Visit Babusar Top and Lulusar Lake.\n");
            sb.append("Day 5: Breakfast and return journey.");
        } else if (destination.equals("Gilgit Adventure")) {
            sb.append("Day 1: Arrival in Gilgit. Visit City Park.\n");
            sb.append("Day 2: Drive to Naltar Valley. Visit Blue Lake.\n");
            sb.append("Day 3: Ski resort visit (if winter) or hiking in Naltar.\n");
            sb.append("Day 4: Visit Kargah Buddha rock carving.\n");
            sb.append("Day 5: Explore Danyore Suspension Bridge.\n");
            sb.append("Day 6: Shopping for dry fruits & Departure.");
        } else if (destination.equals("Kashmir Neelum Valley")) {
            sb.append("Day 1: Drive to Muzaffarabad, then Kutton.\n");
            sb.append("Day 2: Visit Keran and Upper Neelum.\n");
            sb.append("Day 3: Drive to Sharda. Visit Sharda Peeth.\n");
            sb.append("Day 4: Jeep trek to Kel and Arang Kel.\n");
            sb.append("Day 5: Return journey to Islamabad.");
        } else if (destination.equals("Swat Valley (Switzerland of East)")) {
            sb.append("Day 1: Drive to Mingora/Saidu Sharif.\n");
            sb.append("Day 2: Visit Malam Jabba Ski Resort.\n");
            sb.append("Day 3: Drive to Kalam Valley.\n");
            sb.append("Day 4: Jeep ride to Mahodand Lake.\n");
            sb.append("Day 5: Visit White Palace & Departure.");
        } else if (destination.equals("Fairy Meadows Trek")) {
            sb.append("Day 1: Drive to Raikot Bridge.\n");
            sb.append("Day 2: Jeep to Tattu, Trek to Fairy Meadows.\n");
            sb.append("Day 3: Full day at Fairy Meadows with Nanga Parbat view.\n");
            sb.append("Day 4: Hike towards Beyal Camp (optional).\n");
            sb.append("Day 5: Trek down and return journey.");
        } else if (destination.equals("Chitral & Kalash Valley")) {
            sb.append("Day 1: Drive to Chitral via Lowari Tunnel.\n");
            sb.append("Day 2: Visit Chitral Fort and Shahi Mosque.\n");
            sb.append("Day 3: Drive to Kalash Valley (Bumburet).\n");
            sb.append("Day 4: Explore Kalash culture and museum.\n");
            sb.append("Day 5: Visit Rumbur Valley.\n");
            sb.append("Day 6: Return drive to Islamabad.");
        } else if (destination.equals("Murree & Galiyat")) {
            sb.append("Day 1: Drive to Murree. Walk on Mall Road.\n");
            sb.append("Day 2: Day trip to Nathia Gali & Ayubia.\n");
            sb.append("Day 3: Chairlift at Patriata & Departure.");
        } else if (destination.equals("Gwadar Beach & Ormara")) {
            sb.append("Day 1: Drive from Karachi to Ormara via Coastal Highway.\n");
            sb.append("Day 2: Drive to Gwadar. Visit Princess of Hope.\n");
            sb.append("Day 3: Gwadar Port, Hammerhead, and Sunset point.\n");
            sb.append("Day 4: Return journey via Kund Malir Beach.");
        }

        // Create a SpannableString to make the link clickable
        String fullText = sb.toString();
        SpannableString spannableString = new SpannableString(fullText);

        String linkText = "Click here to read on Wikipedia";
        int startIndex = fullText.indexOf(linkText);
        int endIndex = startIndex + linkText.length();

        if (startIndex >= 0) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl));
                    startActivity(browserIntent);
                }
            };
            spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvResult.setText(spannableString);
    }
}