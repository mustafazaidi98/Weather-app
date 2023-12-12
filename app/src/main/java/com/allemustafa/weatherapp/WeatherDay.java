package com.allemustafa.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class WeatherDay implements Serializable {
    private final String Address;
    private final String Icon;
    private final String Description;
    private final String Date;
    private final double Temperature;
    private final double tempMax;
    private final double tempMin;
    private final double preciprob;
    private final double FeelsLike;
    private final double Humidity;
    private final double UVIndex;
    private final double  windspeed;
    private final double visibility;
    private final double cloudcover;
    private final String conditions;

    private final double MorningTemp;
    private final double AfternoonTemp;
    private final double EveningTemp;
    private final double NightTemp;
    private final double windgust;
    private final double windir;
    private final String sunriseTime;
    private final String sunsetTime;
    private final String dayD;
    private final String tempUnit;
    public WeatherDay(String address, String icon, String date, double tempurature,
                      double feelsLike, double humidity, double uvIndex,
                      double windspeed, double visibility, double cloudcover,
                      String conditions, double morningTemp, double afternoonTemp,
                      double eveningTemp, double nightTemp, String description,
                      double windgust, double windir, String sunsetTime, String sunriseTime,
                      double tempMax, double tempMin, double preciprob, String dayD, String tempUnit) {
        Address = address;
        Icon = icon;
        Date = date;
        this.dayD = dayD;
        Temperature = tempurature;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.preciprob = preciprob;
        FeelsLike = feelsLike;
        Humidity = humidity;
        UVIndex = uvIndex;
        this.windspeed = windspeed;
        this.visibility = visibility;
        this.cloudcover = cloudcover;
        this.conditions = conditions;
        MorningTemp = morningTemp;
        AfternoonTemp = afternoonTemp;
        EveningTemp = eveningTemp;
        NightTemp = nightTemp;
        Description = description;
        this.windgust = windgust;
        this.windir = windir;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.tempUnit = tempUnit;
    }

    public String getAddress() {
        return Address;
    }

    public String getDate() {
        return Date;
    }

    public double getTemperature() {
        return Temperature;
    }

    public double getFeelsLike() {
        return FeelsLike;
    }

    public double getHumidity() {
        return Humidity;
    }

    public double getUVIndex() {
        return UVIndex;
    }

    public double getMorningTemp() {
        return MorningTemp;
    }

    public double getAfternoonTemp() {
        return AfternoonTemp;
    }

    public double getEveningTemp() {
        return EveningTemp;
    }

    public double getNightTemp() {
        return NightTemp;
    }
    public JSONObject toJSON() {

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Address", getAddress());
            jsonObject.put("Date", getDate());
            jsonObject.put("Temperature", getTemperature());
            jsonObject.put("FeelsLike", getFeelsLike());
            jsonObject.put("Humidity", getHumidity());
            jsonObject.put("UVIndex", getUVIndex());
            jsonObject.put("MorningTemp", getMorningTemp());
            jsonObject.put("AfternoonTemp", getAfternoonTemp());
            jsonObject.put("EveningTemp", getEveningTemp());
            jsonObject.put("NightTemp", getNightTemp());
            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public String getConditions() {
        return conditions;
    }

    public double getCloudcover() {
        return cloudcover;
    }

    public double getVisibility() {
        return visibility;
    }

    public String getDescription() {
        return Description;
    }

    public String getIcon() {
        return Icon;
    }

    public double getWindgust() {
        return windgust;
    }

    public double getWindir() {
        return windir;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public double getTempMax() {
        return tempMax;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getPreciprob() {
        return preciprob;
    }

    public String getDayD() {
        return dayD;
    }

    public String getTempUnit() {
        return tempUnit;
    }
    public String getSpeedUnit() {
        return tempUnit=="°F"?"mph":"kph";
    }
    public String getdistantUnit() {
        return tempUnit=="°F"?"mi":"km";
    }
}
