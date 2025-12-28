package com.example.intellitour;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AiPlannerActivity extends AppCompatActivity {

    private static final String TAG = "AiPlannerActivity";
    private Interpreter tflite;

    private static final Map<String, Object[]> PACKAGES = new HashMap<>();

    static {
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
    
    private static final String[] PACKAGE_NAMES = {"Murree & Galiyat", "Gwadar Beach & Ormara", "Swat Valley (Switzerland of East)", "Naran & Kaghan", "Fairy Meadows Trek", "Kashmir Neelum Valley", "Gilgit Adventure", "Chitral & Kalash Valley", "Hunza Valley", "Skardu & Deosai"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_planner);

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            Log.e(TAG, "Error loading TFLite model", e);
            Toast.makeText(this, "Error loading AI model. Make sure 'recommendation_model.tflite' is in the assets folder.", Toast.LENGTH_LONG).show();
        }

        EditText etBudget = findViewById(R.id.et_budget);
        EditText etDays = findViewById(R.id.et_days);
        Button btnGenerate = findViewById(R.id.btn_generate_itinerary);
        TextView tvResult = findViewById(R.id.tv_result);

        tvResult.setMovementMethod(LinkMovementMethod.getInstance());

        btnGenerate.setOnClickListener(v -> {
            if (tflite == null) {
                Toast.makeText(this, "AI model is not loaded.", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String budgetInput = etBudget.getText().toString().trim();
            String daysInput = etDays.getText().toString().trim();

            if (budgetInput.isEmpty() || daysInput.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            float budget = Float.parseFloat(budgetInput);
            float days = Float.parseFloat(daysInput);

            String recommendedPackage = recommendPackage(budget, days);
            displayItineraryWithLink(tvResult, recommendedPackage, (int)days, (int)budget);
        });
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("recommendation_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private String recommendPackage(float budget, float days) {
        float[][] input = new float[1][2];
        input[0][0] = budget;
        input[0][1] = days;

        float[][] output = new float[1][10];
        tflite.run(input, output);

        Log.d(TAG, "Model output: " + Arrays.toString(output[0]));

        int maxIndex = 0;
        float maxProbability = -1;
        for (int i = 0; i < output[0].length; i++) {
            if (output[0][i] > maxProbability) {
                maxProbability = output[0][i];
                maxIndex = i;
            }
        }
        Log.d(TAG, "Recommendation index: " + maxIndex);
        return PACKAGE_NAMES[maxIndex];
    }

    private void displayItineraryWithLink(TextView tvResult, String destination, int days, int budget) {
        Object[] packageDetails = PACKAGES.get(destination);
        if (packageDetails == null) {
            tvResult.setText("Could not find details for the recommended package: " + destination);
            return;
        }

        String hotel = (String) packageDetails[2];
        String wikiUrl = (String) packageDetails[3];

        StringBuilder sb = new StringBuilder();
        sb.append("Suggested Destination: ").append(destination).append("\n");
        sb.append("Based on Budget: Rs. ").append(budget).append(" | Duration: ").append(days).append(" Days\n");
        sb.append("Recommended Hotel: ").append(hotel).append("\n");
        sb.append("More Info: Click here to read on Wikipedia").append("\n\n");
        sb.append("--- ITINERARY ---\n\n");

        // ... (rest of the itinerary logic)
        if (destination.equals("Hunza Valley")) {
            sb.append("Day 1: Arrival in Gilgit, drive to Hunza.\n");
            sb.append("Day 2: Visit Karimabad, Baltit Fort, Altit Fort.\n");
            sb.append("Day 3: Eagle's Nest sunrise & Duikar.\n");
            sb.append("Day 4: Day trip to Attabad Lake & Passu Cones.\n");
        } else if (destination.equals("Skardu & Deosai")) {
            sb.append("Day 1: Fly to Skardu, check-in.\n");
            sb.append("Day 2: Shangrila Resort (Lower Kachura Lake).\n");
            sb.append("Day 3: Upper Kachura Lake & boating.\n");
        } else if (destination.equals("Naran & Kaghan")) {
            sb.append("Day 1: Drive from Islamabad to Naran.\n");
            sb.append("Day 2: Jeep ride to Saif-ul-Malook Lake.\n");
        } else {
            sb.append("Detailed itinerary available upon booking.");
        }

        String fullText = sb.toString();
        SpannableString spannableString = new SpannableString(fullText);
        String linkText = "Click here to read on Wikipedia";
        int startIndex = fullText.indexOf(linkText);

        if (startIndex >= 0) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl));
                    startActivity(browserIntent);
                }
            };
            spannableString.setSpan(clickableSpan, startIndex, startIndex + linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvResult.setText(spannableString);
    }

    @Override
    protected void onDestroy() {
        if (tflite != null) {
            tflite.close();
        }
        super.onDestroy();
    }
}