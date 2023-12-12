package com.allemustafa.weatherapp;

import static androidx.recyclerview.widget.LinearLayoutManager.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Icon;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String Location="";
    public boolean fahrenheit = true;
    private TextView date;
    private TextView temp;
    private TextView feelslike;
    private TextView clouds;
    private TextView humidity;
    private TextView winds;
    private TextView UVindex;
    private TextView visibility;
    private TextView morning;
    private TextView afternoon;
    private TextView evening;
    private TextView night;
    private TextView lmorning;
    private TextView lafternoon;
    private TextView levening;
    private TextView lnight;
    private TextView sunsetTime;
    private TextView sunriseTime;;
    private ImageView icon;
    private Weather weather;
    private RecyclerView rcV;
    private HourlyAdapter mAdapter;
    private SwipeRefreshLayout swiper;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Menu mMenuItem;
    MainActivity mainAct;

    public boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
        if(connected==false) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            date.setText("No Internet Connection");
        }
        return connected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        initializeSwiper();
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
        AssignEmptyValues();
        loadSettings();
        if(Location.isEmpty()){
            date.setText("No Location Selected");
        }
    }

    private void initializeComponents() {
        mainAct = this;
        getSupportActionBar().setBackgroundDrawable
                (new ColorDrawable(Color.parseColor("#072B5F")));
        feelslike = findViewById(R.id.feelsLike);
        icon = findViewById(R.id.imageView);
        clouds = findViewById(R.id.cloudCover);
        date = findViewById(R.id.DateTime);
        humidity = findViewById(R.id.Humidity);
        morning = findViewById(R.id.Morning);
        afternoon = findViewById(R.id.Afternoon);
        evening = findViewById(R.id.Evening);
        night = findViewById(R.id.Night);
        lmorning = findViewById(R.id.MorningLabel1);
        lafternoon = findViewById(R.id.AfternoonLabel1);
        levening = findViewById(R.id.EveningLabel1);
        lnight = findViewById(R.id.Nightlabel1);
        visibility = findViewById(R.id.Visibility);
        UVindex = findViewById(R.id.UVindex);
        temp = findViewById(R.id.Temperature);
        winds = findViewById(R.id.Wind);
        sunriseTime = findViewById(R.id.sunrise);
        sunsetTime = findViewById(R.id.sunset);
        rcV = findViewById(R.id.recyclerHour);
    }

    private void initializeSwiper() {
        swiper = findViewById(R.id.wind);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WeatherDataGetter.downloadWeather(mainAct, Location, fahrenheit);
                swiper.setRefreshing(false); // This stops the busy-circle
            }
        });
    }

    private void showLocationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setText(Location);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);
        // lambda can be used here (as is below)
        builder.setPositiveButton("OK", (dialog, id) -> {
            Location = et.getText().toString().
                    replaceAll(", ", ",");
            saveSettings();
            getSupportActionBar().setTitle(Location);
            WeatherDataGetter.downloadWeather(this, Location, fahrenheit);
        });

        // lambda can be used here (as is below)
        builder.setNegativeButton("Cancel", (dialog, id) -> {
        });

        builder.setMessage("for US locations, enter as 'City' or 'City,State'.\n" +
                "For International Write 'City,Country'.");
        builder.setTitle("Enter the Location");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.location) {
            if (hasNetworkConnection())
                showLocationDialog();
        } else if (item.getItemId() == R.id.daily) {
            if (hasNetworkConnection())
                openDailyActivity();
        } else if (item.getItemId() == R.id.unit) {
            setUnitIcon(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUnitIcon(@NonNull MenuItem item) {
        if (hasNetworkConnection()) {
            fahrenheit = !fahrenheit;
            int iconID = -1;
            iconID = getIconID();
            item.setIcon(iconID);
            saveSettings();
            WeatherDataGetter.downloadWeather(this, Location, fahrenheit);
        }
    }

    private int getIconID() {
        int iconID;
        if (fahrenheit)
            iconID = getResources().getIdentifier("units_f", "drawable", getPackageName());
        else
            iconID = getResources().getIdentifier("units_c", "drawable", getPackageName());
        return iconID;
    }

    private void loadSettings() {
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.filename));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            Location = jsonObject.getString("Location");
            fahrenheit = jsonObject.getBoolean("fahrenheit");
            getSupportActionBar().setTitle(Location);
            WeatherDataGetter.downloadWeather(this, Location, fahrenheit);
        } catch (FileNotFoundException ex) {
            return;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    private void saveSettings() {
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.filename), Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fos);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Location",Location);
            jsonObject.put("fahrenheit",fahrenheit);
            printWriter.print(jsonObject);
            printWriter.close();
            fos.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    public void openDailyActivity() {
        Intent intent = new Intent(this, DailyWeatherActivity.class);
        intent.putExtra("weather", weather);
        activityResultLauncher.launch(intent);
    }
    private void handleResult(ActivityResult result) {
        if (result == null || result.getData() == null) {
            return;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        mMenuItem = menu;
        int iconID = -1;
        iconID = getIconID();
        MenuItem it = mMenuItem.findItem(R.id.unit);
        it.setIcon(iconID);
        return true;
    }
    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }
    public void AssignUIValues(){
        WeatherDay day = weather.getDays().get(0);
        date.setText(weather.getFetchTime());
        temp.setText(day.getTemperature()+day.getTempUnit());
        feelslike.setText("Feels like "+day.getFeelsLike()+day.getTempUnit());
        clouds.setText(day.getConditions()+" ("+day.getCloudcover()+"% clouds)");
        winds.setText("Winds: "+getDirection(day.getWindir())+" at "+day.getWindspeed()+" "+day.getdistantUnit()+" gusting to " +
                day.getWindgust()+" "+day.getdistantUnit());
        humidity.setText("Humidity: "+day.getHumidity()+"%");
        UVindex.setText("UV Index: "+day.getUVIndex());
        visibility.setText("Visibility: "+day.getVisibility()+" "+day.getSpeedUnit());
        morning.setText(day.getMorningTemp()+day.getTempUnit());
        afternoon.setText(day.getAfternoonTemp()+day.getTempUnit());
        evening.setText(day.getEveningTemp()+day.getTempUnit());
        night.setText(day.getNightTemp()+day.getTempUnit());
        sunriseTime.setText("Sunrise at: "+day.getSunriseTime());
        sunsetTime.setText("Sunset at: "+day.getSunsetTime());
        lafternoon.setText("Afternoon");
        levening.setText("Evening");
        lmorning.setText("Morning");
        lnight.setText("Night");
        String ic = day.getIcon().replace("-", "_"); // Replace all dashes with underscores
        int iconID = getResources().getIdentifier(ic, "drawable", getPackageName());

        if (iconID != 0) {
            icon.setImageResource(iconID);
        }
        mAdapter = new HourlyAdapter(weather.getHours(), this);
        rcV.setAdapter(mAdapter);
        rcV.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL,false));
    }
    public void AssignEmptyValues(){
        getSupportActionBar().setTitle("Weather App");
        temp.setText("");//for farhenheit
        feelslike.setText("");
        clouds.setText("");
        winds.setText("");
        humidity.setText("");
        UVindex.setText("");
        visibility.setText("");
        morning.setText("");
        afternoon.setText("");
        evening.setText("");
        night.setText("");
        sunriseTime.setText("");
        sunsetTime.setText("");
        lafternoon.setText("");
        levening.setText("");
        lmorning.setText("");
        lnight.setText("");
        icon.setImageResource(0);
        mAdapter = new HourlyAdapter(new ArrayList<WeatherHours>(), this);
        rcV.setAdapter(mAdapter);
        rcV.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL,false));
    }
    public void updateData(Weather weather) {
        this.weather = weather;
        AssignUIValues();
    }

    public void InvalidLocationSelected() {
        date.setText("Invalid Location Selected");
        Toast.makeText(this, "Can not find specified location.", Toast.LENGTH_SHORT).show();
        AssignEmptyValues();
    }
}