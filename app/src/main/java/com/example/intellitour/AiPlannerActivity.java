package com.example.intellitour;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AiPlannerActivity extends AppCompatActivity {

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
            String destination = etDestination.getText().toString();
            String budget = etBudget.getText().toString();
            String days = etDays.getText().toString();

            if (destination.isEmpty() || budget.isEmpty() || days.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mock AI Response for now
            String itinerary = "Generated Itinerary for " + destination + ":\n\n" +
                    "Day 1: Arrival and City Tour\n" +
                    "Day 2: Cultural Heritage Sites\n" +
                    "Day 3: Shopping and Departure\n\n" +
                    "Estimated Cost: $" + budget;
            
            tvResult.setText(itinerary);
        });
    }
}