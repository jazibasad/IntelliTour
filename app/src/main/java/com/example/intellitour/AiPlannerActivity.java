package com.example.intellitour;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class AiPlannerActivity extends AppCompatActivity {

    // Define the valid packages with their details (Name -> [Min Budget, Min Days])
    private static final Map<String, int[]> PACKAGES = new HashMap<>();

    static {
        // Destination Name (lowercase for easy matching) -> {Price, Days}
        PACKAGES.put("hunza", new int[]{75000, 7});
        PACKAGES.put("hunza valley", new int[]{75000, 7});
        PACKAGES.put("skardu", new int[]{95000, 8});
        PACKAGES.put("deosai", new int[]{95000, 8});
        PACKAGES.put("naran", new int[]{50000, 5});
        PACKAGES.put("kaghan", new int[]{50000, 5});
        PACKAGES.put("naran kaghan", new int[]{50000, 5});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_planner);

        EditText etDestination = findViewById(R.id.et_destination);
        EditText etBudget = findViewById(R.id.et_budget);
        EditText etDays = findViewById(R.id.et_days);
        Button btnGenerate = findViewById(R.id.btn_generate_itinerary);
        TextView tvResult = findViewById(R.id.tv_result);

        btnGenerate.setOnClickListener(v -> {
            String destinationInput = etDestination.getText().toString().trim().toLowerCase();
            String budgetInput = etBudget.getText().toString().trim();
            String daysInput = etDays.getText().toString().trim();

            if (destinationInput.isEmpty() || budgetInput.isEmpty() || daysInput.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if destination is valid
            if (!isValidDestination(destinationInput)) {
                tvResult.setText("Sorry, we currently only offer packages for:\n- Hunza Valley\n- Skardu & Deosai\n- Naran & Kaghan\n\nPlease check the 'Packages' screen for details.");
                return;
            }

            int budget = Integer.parseInt(budgetInput);
            int days = Integer.parseInt(daysInput);

            // Validate constraints for the specific destination
            String validPackageName = getPackageName(destinationInput);
            int[] packageDetails = getPackageDetails(destinationInput);
            int minPrice = packageDetails[0];
            int packageDays = packageDetails[1];

            if (budget < minPrice) {
                tvResult.setText("Budget too low for " + validPackageName + ".\nMinimum required: Rs. " + minPrice);
                return;
            }

            if (days < packageDays) {
                tvResult.setText("Duration too short for " + validPackageName + ".\nRecommended duration: " + packageDays + " days.");
                return;
            }

            // If all checks pass, generate the itinerary
            String itinerary = generateItinerary(validPackageName, days, budget);
            tvResult.setText(itinerary);
        });
    }

    private boolean isValidDestination(String input) {
        for (String key : PACKAGES.keySet()) {
            if (input.contains(key)) return true;
        }
        return false;
    }

    private String getPackageName(String input) {
        if (input.contains("hunza")) return "Hunza Valley";
        if (input.contains("skardu") || input.contains("deosai")) return "Skardu & Deosai";
        if (input.contains("naran") || input.contains("kaghan")) return "Naran & Kaghan";
        return "Unknown";
    }

    private int[] getPackageDetails(String input) {
        if (input.contains("hunza")) return PACKAGES.get("hunza");
        if (input.contains("skardu")) return PACKAGES.get("skardu");
        if (input.contains("deosai")) return PACKAGES.get("deosai");
        // Default to Naran if matched via logic but specific key needed
        return PACKAGES.get("naran");
    }

    private String generateItinerary(String destination, int days, int budget) {
        StringBuilder sb = new StringBuilder();
        sb.append("AI Itinerary for ").append(destination).append("\n");
        sb.append("Budget: Rs. ").append(budget).append(" | Duration: ").append(days).append(" Days\n\n");

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

        return sb.toString();
    }
}