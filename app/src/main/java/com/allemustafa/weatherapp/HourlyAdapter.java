package com.allemustafa.weatherapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyViewHolder> {

    public final List<WeatherHours> hours;
    public final Activity mainActivity;

    public HourlyAdapter(List<WeatherHours> hours, Activity mainActivity) {
        this.hours = hours;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_ui, parent, false);
        return new HourlyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        WeatherHours hour = hours.get(position);
        int iconID = mainActivity.getResources().getIdentifier(hour.getIcon(), "drawable", mainActivity.getPackageName());
        if (iconID != 0) {
            holder.iconHourly.setImageResource(iconID);
        }
        holder.DayHourly.setText(hour.getDay());
        holder.conditionHourly.setText(hour.getDescription());
        holder.TemperatureHourly.setText(hour.getTemperature()+hour.getTempUnit());
        holder.DateHourly.setText(hour.getTime());
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }
}
