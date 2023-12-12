package com.allemustafa.weatherapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DailyViewHolder extends RecyclerView.ViewHolder {
    TextView DayHourlydaily;
    TextView Temperaturedaily;
    TextView Conditiondaily;
    TextView Percepdaily;
    TextView UVIndexdaily;
    TextView Morningdaily;
    TextView Afternoondaily;
    TextView Eveningdaily;
    TextView Nightdaily;
    ImageView Icondaily;


    public DailyViewHolder(@NonNull View itemView) {
        super(itemView);
        this.DayHourlydaily = itemView.findViewById(R.id.dateDaily);
        this.Temperaturedaily = itemView.findViewById(R.id.temperatureDaily);
        this.Conditiondaily = itemView.findViewById(R.id.conditionsDaily);
        this.Percepdaily = itemView.findViewById(R.id.PercipDaily);
        this.UVIndexdaily = itemView.findViewById(R.id.UVIndexdaily);
        this.Morningdaily = itemView.findViewById(R.id.morningDaily);
        this.Eveningdaily = itemView.findViewById(R.id.eveningDaily);
        this.Afternoondaily = itemView.findViewById(R.id.afternoonDaily);
        this.Nightdaily = itemView.findViewById(R.id.nightDaily);
        this.Icondaily = itemView.findViewById(R.id.iconDaily);
    }
}
