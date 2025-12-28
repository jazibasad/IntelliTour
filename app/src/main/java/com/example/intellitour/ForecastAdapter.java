package com.example.intellitour;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<ForecastResponse.ForecastItem> forecastItems;

    public ForecastAdapter(List<ForecastResponse.ForecastItem> forecastItems) {
        this.forecastItems = forecastItems;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastResponse.ForecastItem item = forecastItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return forecastItems.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDay, tvCondition, tvTemp;
        private ImageView ivIcon;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_forecast_day);
            tvCondition = itemView.findViewById(R.id.tv_forecast_condition);
            tvTemp = itemView.findViewById(R.id.tv_forecast_temp);
            ivIcon = itemView.findViewById(R.id.iv_forecast_icon);
        }

        public void bind(ForecastResponse.ForecastItem item) {
            tvTemp.setText(String.format(Locale.getDefault(), "%.0f°C", item.main.temp));
            
            try {
                SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                Date date = apiFormat.parse(item.dateTimeText);
                tvDay.setText(displayFormat.format(date));
            } catch (ParseException e) {
                tvDay.setText("Unknown Day");
            }

            if (!item.weather.isEmpty()) {
                String condition = item.weather.get(0).mainCondition;
                tvCondition.setText(condition);
                
                String conditionLc = condition.toLowerCase();
                if (conditionLc.contains("clear")) {
                    ivIcon.setImageResource(android.R.drawable.ic_menu_day);
                } else if (conditionLc.contains("cloud")) {
                    ivIcon.setImageResource(android.R.drawable.ic_menu_day);
                } else if (conditionLc.contains("rain")) {
                    ivIcon.setImageResource(android.R.drawable.ic_menu_day);
                } else {
                    ivIcon.setImageResource(android.R.drawable.ic_menu_help);
                }
            }
        }
    }
}