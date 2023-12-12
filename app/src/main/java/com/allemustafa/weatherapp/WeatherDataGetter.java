package com.allemustafa.weatherapp;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherDataGetter {
    private static final String TAG = "Weather Download";
    private static MainActivity mainActivity;
    private static RequestQueue queue;

    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private static final String iconUrl = "https://openweathermap.org/img/w/";
    private static final String yourAPIKey = "W46NR7J9R784DPSBN8ZJ2XHTC";


    public static void downloadWeather(MainActivity mainActivityIn,
                                       String city, boolean fahrenheit) {
        mainActivity = mainActivityIn;
        if(mainActivity.hasNetworkConnection()==false){
            return;
        }
        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();
        buildURL.appendPath(city);
        buildURL.appendQueryParameter("unitGroup", (fahrenheit ? "us" : "metric"));
        buildURL.appendQueryParameter("key", yourAPIKey);
        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> parseJSON(response);

        Response.ErrorListener error =
                error1 -> mainActivity.InvalidLocationSelected();

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
    private static WeatherDay GetDayData(JSONObject cc, String resolvedAddress,String tempUnit) throws JSONException {
        long dt = cc.getLong("datetimeEpoch");
        Date dateTime = new Date(dt * 1000);
        SimpleDateFormat fullDate =
                new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE MM/dd", Locale.getDefault());
        String dayD = dayDate.format(dateTime);
        String fullDateStr = fullDate.format(dateTime);
        long srt = cc.getLong("sunriseEpoch");
        Date srtTime = new Date(srt * 1000);
        String sunriseTime = timeOnly.format(srtTime);
        long sst = cc.getLong("sunsetEpoch");
        Date sstTime = new Date(sst * 1000);
        String sunsetTime = timeOnly.format(sstTime);
        double temp = cc.getDouble("temp");
        double tempMin = cc.getDouble("tempmin");
        double tempMax = cc.getDouble("tempmax");
        double preciprob = cc.getDouble("precipprob");
        double feelsLike = cc.getDouble("feelslike");
        double humidity = cc.getDouble("humidity");
        double uvindex = cc.getDouble("uvindex");
        double windspeed = cc.getDouble("windspeed");
        double windir = cc.getDouble("winddir");
        double visibility = cc.getDouble("visibility");
        double cloudcover = cc.getDouble("cloudcover");
        String icon = cc.getString("icon");
        String condition = cc.getString("conditions");
        double windgust = cc.getDouble("windgust");
        String description = cc.getString("description");
        JSONArray hours = cc.getJSONArray("hours");
        JSONObject morninghour = (JSONObject) hours.get(8);
        double morningTemp = morninghour.getDouble("temp");
        JSONObject afternoonhour = (JSONObject) hours.get(13);
        double afternoonTemp = afternoonhour.getDouble("temp");
        JSONObject eveninghour = (JSONObject) hours.get(17);
        double eveningTemp = eveninghour.getDouble("temp");
        double nightTemp = eveningTemp;
        if(hours.length()>23) {
            JSONObject nighthour = (JSONObject) hours.get(23);
            nightTemp = nighthour.getDouble("temp");
        }
        WeatherDay weather = new WeatherDay(resolvedAddress, icon, fullDateStr,
                temp, feelsLike, humidity, uvindex,
                windspeed, visibility, cloudcover, condition,
                morningTemp, afternoonTemp,
                eveningTemp, nightTemp,description,
                windgust,windir,sunsetTime,sunriseTime,
                tempMax,tempMin,preciprob,dayD,tempUnit);
        return weather;
    }
    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }
    private static WeatherHours GetHours(JSONObject hours,String tempUnit) throws JSONException {
        long dt = hours.getLong("datetimeEpoch");
        Date dateTime = new Date(dt * 1000); // Java time values need milliseconds
        if((new Date().compareTo(dateTime))>=0)
            return null;
        SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
        SimpleDateFormat day = new SimpleDateFormat("EEEE", Locale.getDefault());
        String timeOnlyStr = timeOnly.format(dateTime); // 12:00 AM
        String dayStr = isSameDay(new Date(),dateTime)?"Today":day.format(dateTime); // Thursday
        String icon = hours.getString("icon");
        double temperature = hours.getDouble("temp");
        String conditions = hours.getString("conditions");
        return new WeatherHours(dayStr, timeOnlyStr, icon
                , conditions, temperature,tempUnit);
    }
    private static void parseJSON(JSONObject obj) {
        try {
            JSONArray days = obj.getJSONArray("days");
            String resolvedAddress = obj.getString("resolvedAddress");
            List<WeatherDay> daysData = new ArrayList<WeatherDay>();
            List<WeatherHours> hoursData = new ArrayList<WeatherHours>();
            String tempUnit = mainActivity.fahrenheit?"°F":"°C";
            int k=0;
            for (int i = 0; i < days.length(); i++) {
                JSONObject dy = (JSONObject) days.get(i);
                WeatherDay w = GetDayData(dy, resolvedAddress,tempUnit);
                daysData.add(w);
                JSONArray hours = dy.getJSONArray("hours");
                for (int j = 0; j < hours.length() && k<48; j++) {
                    WeatherHours hour = GetHours((JSONObject) hours.get(j),tempUnit);
                    if(hour!=null) {
                        hoursData.add(hour);
                        k++;
                    }
                }
            }
            Weather weather = new Weather(daysData,hoursData);
            mainActivity.updateData(weather);
        } catch (JSONException ex) {
            mainActivity.InvalidLocationSelected();
        }

    }
}

