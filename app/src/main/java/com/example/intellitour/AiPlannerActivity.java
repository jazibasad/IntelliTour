package com.example.intellitour;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

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

    private static final float BUDGET_MEAN = 56000.0f;
    private static final float BUDGET_STD_DEV = 19339.08f;
    private static final float DAYS_MEAN = 5.4f;
    private static final float DAYS_STD_DEV = 1.356f;

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
        
        MaterialCardView resultCard = findViewById(R.id.itinerary_card_result);
        TextView tvPlaceholder = findViewById(R.id.tv_result_placeholder);

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

            tvPlaceholder.setVisibility(View.GONE);
            resultCard.setVisibility(View.VISIBLE);
            displayItinerary(resultCard, recommendedPackage);
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
        float scaledBudget = (budget - BUDGET_MEAN) / BUDGET_STD_DEV;
        float scaledDays = (days - DAYS_MEAN) / DAYS_STD_DEV;

        float[][] input = new float[1][2];
        input[0][0] = scaledBudget;
        input[0][1] = scaledDays;

        float[][] output = new float[1][10];
        tflite.run(input, output);

        int maxIndex = 0;
        float maxProbability = -1;
        for (int i = 0; i < output[0].length; i++) {
            if (output[0][i] > maxProbability) {
                maxProbability = output[0][i];
                maxIndex = i;
            }
        }
        return PACKAGE_NAMES[maxIndex];
    }

    private void displayItinerary(MaterialCardView card, String destination) {
        Object[] packageDetails = PACKAGES.get(destination);
        if (packageDetails == null) {
            Toast.makeText(this, "Could not find details for " + destination, Toast.LENGTH_SHORT).show();
            return;
        }

        int actualPrice = (int) packageDetails[0];
        int actualDuration = (int) packageDetails[1];
        String hotel = (String) packageDetails[2];
        String wikiUrl = (String) packageDetails[3];

        TextView tvDestinationName = card.findViewById(R.id.tv_destination_name);
        TextView tvBudgetInfo = card.findViewById(R.id.tv_budget_info);
        TextView tvDurationInfo = card.findViewById(R.id.tv_duration_info);
        TextView tvHotelInfo = card.findViewById(R.id.tv_hotel_info);
        TextView tvItineraryDetails = card.findViewById(R.id.tv_itinerary_details);
        TextView tvWikiLink = card.findViewById(R.id.tv_wiki_link);

        tvDestinationName.setText(destination);
        tvBudgetInfo.setText("Price: Rs. " + actualPrice);
        tvDurationInfo.setText("Duration: " + actualDuration + " Days");
        tvHotelInfo.setText("Stay at: " + hotel);
        tvWikiLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl));
            startActivity(browserIntent);
        });

        StringBuilder itinerary = new StringBuilder();
        switch (destination) {
            case "Murree & Galiyat":
                itinerary.append("Day 1: Drive to Murree, check-in, and evening walk on Mall Road.\n");
                itinerary.append("Day 2: Day trip to Nathia Gali, Ayubia, and Dunga Gali.\n");
                itinerary.append("Day 3: Visit Patriata (New Murree) for chairlift, then departure.");
                break;
            case "Gwadar Beach & Ormara":
                itinerary.append("Day 1: Drive from Karachi via Coastal Highway, stop at Kund Malir Beach, check-in at Ormara.\n");
                itinerary.append("Day 2: Drive to Gwadar, visit Princess of Hope and the Sphinx.\n");
                itinerary.append("Day 3: Explore Gwadar Port, Hammerhead, and enjoy the sunset.\n");
                itinerary.append("Day 4: Return journey to Karachi.");
                break;
            case "Swat Valley (Switzerland of East)":
                itinerary.append("Day 1: Drive to Mingora/Saidu Sharif, visit Swat Museum.\n");
                itinerary.append("Day 2: Full day excursion to Malam Jabba Ski Resort.\n");
                itinerary.append("Day 3: Drive to Kalam Valley, explore Ushu Forest.\n");
                itinerary.append("Day 4: Jeep ride to the beautiful Mahodand Lake.\n");
                itinerary.append("Day 5: Visit White Palace in Marghazar, then departure.");
                break;
            case "Naran & Kaghan":
                itinerary.append("Day 1: Drive from Islamabad to Naran, check-in.\n");
                itinerary.append("Day 2: Full day jeep excursion to the iconic Saif-ul-Malook Lake.\n");
                itinerary.append("Day 3: Enjoy river rafting in Kunhar River or explore Naran Bazaar.\n");
                itinerary.append("Day 4: Visit Lulusar Lake and Babusar Top for breathtaking views.\n");
                itinerary.append("Day 5: Breakfast and departure for Islamabad.");
                break;
            case "Fairy Meadows Trek":
                itinerary.append("Day 1: Drive from Gilgit to Raikot Bridge.\n");
                itinerary.append("Day 2: Exciting jeep ride to Tattu village, then a 3-4 hour trek to Fairy Meadows.\n");
                itinerary.append("Day 3: Full day to explore Fairy Meadows, with stunning views of Nanga Parbat.\n");
                itinerary.append("Day 4: Optional hike towards Beyal Camp, the base camp of Nanga Parbat.\n");
                itinerary.append("Day 5: Trek down to Tattu and take the jeep back to Raikot Bridge for departure.");
                break;
            case "Kashmir Neelum Valley":
                itinerary.append("Day 1: Drive from Islamabad to Muzaffarabad, then to Keran in Neelum Valley.\n");
                itinerary.append("Day 2: Explore Upper Neelum and enjoy views of the Line of Control.\n");
                itinerary.append("Day 3: Drive to Sharda, visit the ruins of Sharda Peeth university.\n");
                itinerary.append("Day 4: Full day jeep excursion to the pristine hamlets of Kel and Arang Kel.\n");
                itinerary.append("Day 5: Begin return journey towards Islamabad.");
                break;
            case "Gilgit Adventure":
                itinerary.append("Day 1: Arrival in Gilgit, visit the Gilgit City Park.\n");
                itinerary.append("Day 2: Full day excursion to the beautiful Naltar Valley and its colorful lakes.\n");
                itinerary.append("Day 3: Visit the Kargah Buddha, an ancient rock carving.\n");
                itinerary.append("Day 4: Explore the Danyore Suspension Bridge and the local bazaar.\n");
                itinerary.append("Day 5: Day trip towards Rakaposhi View Point.\n");
                itinerary.append("Day 6: Shopping for local handicrafts and dry fruits, then departure.");
                break;
            case "Chitral & Kalash Valley":
                itinerary.append("Day 1: Drive from Islamabad to Chitral via the Lowari Tunnel.\n");
                itinerary.append("Day 2: Explore Chitral town, including the Chitral Fort and the Shahi Mosque.\n");
                itinerary.append("Day 3: Drive to Bumburet, the largest of the Kalash Valleys.\n");
                itinerary.append("Day 4: Immerse yourself in the unique culture of the Kalash people, visit their museum and graveyards.\n");
                itinerary.append("Day 5: Explore the beautiful Rumbur Valley.\n");
                itinerary.append("Day 6: Begin the return journey to Islamabad.");
                break;
            case "Hunza Valley":
                itinerary.append("Day 1: Arrival in Gilgit, drive to Hunza, check-in at Karimabad.\n");
                itinerary.append("Day 2: Explore Karimabad, visit the historic Baltit Fort and Altit Fort.\n");
                itinerary.append("Day 3: Witness sunrise at Eagle's Nest (Duikar), and explore the village.\n");
                itinerary.append("Day 4: Day trip to the stunning Attabad Lake and the iconic Passu Cones.\n");
                itinerary.append("Day 5: Visit the Hussaini Suspension Bridge and explore Gulmit.\n");
                itinerary.append("Day 6: Journey to the Pakistan-China border at Khunjerab Pass.\n");
                itinerary.append("Day 7: Drive back to Gilgit for departure.");
                break;
            case "Skardu & Deosai":
                itinerary.append("Day 1: Arrival in Skardu, check-in and rest.\n");
                itinerary.append("Day 2: Visit Shangrila Resort (Lower Kachura Lake) and Upper Kachura Lake.\n");
                itinerary.append("Day 3: Drive to Shigar, visit the Cold Desert and the historic Shigar Fort.\n");
                itinerary.append("Day 4: Full day jeep safari to the vast Deosai National Park, the 'Land of Giants'.\n");
                itinerary.append("Day 5: Visit Sheosar Lake within Deosai Plains.\n");
                itinerary.append("Day 6: Explore the beautiful Sadpara Lake and Dam.\n");
                itinerary.append("Day 7: Visit Manthokha Waterfall (if accessible).\n");
                itinerary.append("Day 8: Explore Skardu Bazaar for shopping, then departure.");
                break;
            default:
                itinerary.append("No itinerary available for this selection.");
                break;
        }
        tvItineraryDetails.setText(itinerary.toString());
    }

    @Override
    protected void onDestroy() {
        if (tflite != null) {
            tflite.close();
        }
        super.onDestroy();
    }
}