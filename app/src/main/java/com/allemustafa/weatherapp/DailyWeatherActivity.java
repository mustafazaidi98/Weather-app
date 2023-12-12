package com.allemustafa.weatherapp;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class DailyWeatherActivity extends AppCompatActivity {

    RecyclerView dailyWeatherRecycler;
    Weather w;
    private DailyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);
        dailyWeatherRecycler = findViewById(R.id.dailyWeatherRecycler);
        Intent intent = getIntent();
        if (intent.hasExtra("weather")) {
            w = (Weather) intent.getSerializableExtra("weather");
            if (w != null)
            {
                getSupportActionBar().setBackgroundDrawable
                        (new ColorDrawable(Color.parseColor("#072B5F")));
                getSupportActionBar().setTitle(w.getDays().get(0).getAddress()+" 15 Day");
                mAdapter = new DailyAdapter(w.getDays(), this);
                dailyWeatherRecycler.setAdapter(mAdapter);
                dailyWeatherRecycler.setLayoutManager(new LinearLayoutManager(this));
            }
        }
    }
}