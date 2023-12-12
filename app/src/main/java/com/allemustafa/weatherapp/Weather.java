package com.allemustafa.weatherapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Weather implements Serializable {
    private final List<WeatherDay> days;
    private final List<WeatherHours> hours;
    private final String fetchTime;
    public Weather(List<WeatherDay> days, List<WeatherHours> hours){

        this.days = days;
        this.hours = hours;
        SimpleDateFormat fullDate =
                new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        fetchTime = fullDate.format(new Date());;
    }

    public List<WeatherDay> getDays() {
        return days;
    }

    public List<WeatherHours> getHours() {
        return hours;
    }

    public String getFetchTime() {
        return fetchTime;
    }
}
