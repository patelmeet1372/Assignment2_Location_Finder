package com.example.assignment2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText editTextLocation;
    private TextView textViewResult;
    private DatabaseHelper databaseHelper;

    // Shared Preferences keys
    private static final String PREFS_NAME = "DatabasePrefs";
    private static final String KEY_INITIALIZED = "isInitialized";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        editTextLocation = findViewById(R.id.editTextLocation);
        textViewResult = findViewById(R.id.textViewResult);
        Button buttonQuery = findViewById(R.id.buttonQuery);
        Button buttonUpdateDatabase = findViewById(R.id.buttonUpdateDatabase);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Check if database has been initialized with entries
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isInitialized = prefs.getBoolean(KEY_INITIALIZED, false);

        if (!isInitialized) {
            // Populate the database from JSON file
            Log.d("MainActivity", "Database not initialized. Starting population...");
            populateDatabaseFromJson();

            // Set initialization flag to true
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_INITIALIZED, true);
            editor.apply();
        } else {
            Log.d("MainActivity", "Database already initialized.");
        }

        // Query button logic
        buttonQuery.setOnClickListener(v -> {
            String location = editTextLocation.getText().toString().trim();

            // Check if the location field is empty
            if (location.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                return;
            }

            // Query the database for latitude and longitude
            Cursor cursor = databaseHelper.queryLocation(location);
            if (cursor != null && cursor.moveToFirst()) {
                int latIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE);
                int lonIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE);

                if (latIndex != -1 && lonIndex != -1) {
                    double latitude = cursor.getDouble(latIndex);
                    double longitude = cursor.getDouble(lonIndex);
                    textViewResult.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
                } else {
                    textViewResult.setText("Error: Columns not found in database");
                }
                cursor.close();
            } else {
                textViewResult.setText("Location not found in database");
            }
        });

        // Update Database button logic
        buttonUpdateDatabase.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UpdateDatabaseActivity.class);
            startActivity(intent);
        });
    }

    // Method to populate the database from JSON file
    private void populateDatabaseFromJson() {
        try {
            InputStream is = getAssets().open("locations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);

            Log.d("MainActivity", "Populating database with JSON data.");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String city = obj.getString("city");
                double latitude = obj.getDouble("latitude");
                double longitude = obj.getDouble("longitude");

                databaseHelper.addLocation(city, latitude, longitude);
                Log.d("MainActivity", "Inserted city: " + city + " with coordinates (" + latitude + ", " + longitude + ")");
            }
            Log.d("MainActivity", "Database population complete.");
        } catch (Exception e) {
            Log.e("MainActivity", "Error populating database from JSON", e);
        }
    }
}
