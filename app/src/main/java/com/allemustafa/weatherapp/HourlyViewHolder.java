package com.allemustafa.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HourlyViewHolder extends RecyclerView.ViewHolder {
    TextView DayHourly;
    TextView DateHourly;
    TextView TemperatureHourly;
    TextView conditionHourly;
    ImageView iconHourly;
    public HourlyViewHolder(@NonNull View itemView) {
        super(itemView);
        this.conditionHourly = itemView.findViewById(R.id.conditionHourly);
        this.DateHourly = itemView.findViewById(R.id.DateHourly);
        this.DayHourly = itemView.findViewById(R.id.DayHourly);
        this.iconHourly = itemView.findViewById(R.id.iconHourly);
        this.TemperatureHourly = itemView.findViewById(R.id.TemperatureHourly);
    }
}
