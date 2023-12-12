package com.allemustafa.weatherapp;

import java.io.Serializable;

public class WeatherHours implements Serializable {
    private final String Day;
    private final String Time;
    private final String icon;
    private final String description;
    private final double temperature;
    private final String tempUnit;

    public WeatherHours(String day, String time, String icon
            , String description, double temperature,String tempUnit) {
        Day = day;
        Time = time;
        this.icon = icon;
        this.description = description;
        this.temperature = temperature;
        this.tempUnit = tempUnit;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getTime() {
        return Time;
    }

    public String getDay() {
        return Day;
    }

    public String getTempUnit() {
        return tempUnit;
    }
}
