package com.example.assignment2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateDatabaseActivity extends AppCompatActivity {

    private EditText editTextLocation;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_database);

        // Initialize UI components
        editTextLocation = findViewById(R.id.editTextLocationName);
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        Button buttonAdd = findViewById(R.id.buttonAddLocation);
        Button buttonUpdate = findViewById(R.id.buttonUpdateLocation);
        Button buttonDelete = findViewById(R.id.buttonDeleteLocation);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Add button logic
        buttonAdd.setOnClickListener(v -> addLocation());

        // Update button logic
        buttonUpdate.setOnClickListener(v -> updateLocation());

        // Delete button logic
        buttonDelete.setOnClickListener(v -> deleteLocation());
    }

    // Method to add a location to the database
    private void addLocation() {
        String location = editTextLocation.getText().toString().trim();
        String latitudeStr = editTextLatitude.getText().toString().trim();
        String longitudeStr = editTextLongitude.getText().toString().trim();

        if (location.isEmpty() || latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude = Double.parseDouble(latitudeStr);
        double longitude = Double.parseDouble(longitudeStr);

        boolean success = databaseHelper.addLocation(location, latitude, longitude);
        if (success) {
            Toast.makeText(this, "Location added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add location", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to update an existing location in the database
    private void updateLocation() {
        String location = editTextLocation.getText().toString().trim();
        String latitudeStr = editTextLatitude.getText().toString().trim();
        String longitudeStr = editTextLongitude.getText().toString().trim();

        if (location.isEmpty() || latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double latitude = Double.parseDouble(latitudeStr);
        double longitude = Double.parseDouble(longitudeStr);

        boolean success = databaseHelper.updateLocation(location, latitude, longitude);
        if (success) {
            Toast.makeText(this, "Location updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update location", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to delete a location from the database
    private void deleteLocation() {
        String location = editTextLocation.getText().toString().trim();

        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter a location name", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = databaseHelper.deleteLocation(location);
        if (success) {
            Toast.makeText(this, "Location deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete location", Toast.LENGTH_SHORT).show();
        }
    }
}
