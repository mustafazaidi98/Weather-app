package com.allemustafa.weatherapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailyAdapter extends RecyclerView.Adapter<DailyViewHolder> {
    public final List<WeatherDay> days;
    public final Activity mainActivity;

    public DailyAdapter(List<WeatherDay> days, Activity mainActivity) {
        this.days = days;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_ui, parent, false);
        return new DailyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        WeatherDay day = days.get(position);
        int iconID = mainActivity.getResources().getIdentifier(day.getIcon(), "drawable", mainActivity.getPackageName());
        if (iconID != 0) {
            holder.Icondaily.setImageResource(iconID);
        }
        holder.DayHourlydaily.setText(day.getDayD());
        holder.Temperaturedaily.setText(day.getTempMax()+day.getTempUnit()+"/"+day.getTempMax()+day.getTempUnit());
        holder.Conditiondaily.setText(day.getConditions());
        holder.Afternoondaily.setText(day.getAfternoonTemp()+day.getTempUnit());
        holder.Morningdaily.setText(day.getMorningTemp()+day.getTempUnit());
        holder.Eveningdaily.setText(day.getEveningTemp()+day.getTempUnit());
        holder.Nightdaily.setText(day.getNightTemp()+day.getTempUnit());
        holder.Percepdaily.setText("("+day.getPreciprob()+"% percep.)");
        holder.UVIndexdaily.setText("UV index: "+day.getUVIndex());
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
