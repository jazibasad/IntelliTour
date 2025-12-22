package com.example.intellitour;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class AiPlannerActivity extends AppCompatActivity {

    // Define the valid packages with their details (Name -> [Min Budget, Min Days])
    private static final Map<String, int[]> PACKAGES = new HashMap<>();

    static {
        // Destination Name -> {Price, Days}
        PACKAGES.put("Naran & Kaghan", new int[]{50000, 5});
        PACKAGES.put("Hunza Valley", new int[]{75000, 7});
        PACKAGES.put("Skardu & Deosai", new int[]{95000, 8});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_planner);

        EditText etBudget = findViewById(R.id.et_budget);
        EditText etDays = findViewById(R.id.et_days);
        Button btnGenerate = findViewById(R.id.btn_generate_itinerary);
        TextView tvResult = findViewById(R.id.tv_result);

        btnGenerate.setOnClickListener(v -> {
            String budgetInput = etBudget.getText().toString().trim();
            String daysInput = etDays.getText().toString().trim();

            if (budgetInput.isEmpty() || daysInput.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int budget = Integer.parseInt(budgetInput);
            int days = Integer.parseInt(daysInput);

            // Find best matching package
            String bestPackage = suggestPackage(budget, days);

            if (bestPackage == null) {
                tvResult.setText("Sorry, no packages match your criteria.\n" +
                        "Try increasing your budget or changing duration.\n\n" +
                        "Available options start from Rs. 50,000 for 5 days.");
                return;
            }

            // Generate itinerary for the suggested package
            String itinerary = generateItinerary(bestPackage, days, budget);
            tvResult.setText(itinerary);
        });
    }

    private String suggestPackage(int budget, int days) {
        String bestMatch = null;
        int maxPriceFound = -1;

        // Iterate through all packages to find one that fits budget and days
        // We pick the most expensive one within budget to maximize value (or you can pick cheapest)
        for (Map.Entry<String, int[]> entry : PACKAGES.entrySet()) {
            String pkgName = entry.getKey();
            int price = entry.getValue()[0];
            int duration = entry.getValue()[1];

            if (budget >= price && days >= duration) {
                if (price > maxPriceFound) {
                    maxPriceFound = price;
                    bestMatch = pkgName;
                }
            }
        }
        return bestMatch;
    }

    private String generateItinerary(String destination, int days, int budget) {
        StringBuilder sb = new StringBuilder();
        sb.append("Suggested Destination: ").append(destination).append("\n");
        sb.append("Based on Budget: Rs. ").append(budget).append(" | Duration: ").append(days).append(" Days\n\n");
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
        }

        if (days > 7 && destination.equals("Hunza Valley")) {
             sb.append("\n\n* You have extra days! Consider visiting Naltar Valley or exploring Hopper Glacier.");
        } else if (days > 5 && destination.equals("Naran & Kaghan")) {
             sb.append("\n\n* You have extra days! Consider visiting Shogran and Siri Paye Meadows.");
        }

        return sb.toString();
    }
}