package com.vahagn.barber_line.Activities.Admin;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.vahagn.barber_line.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker currentMarker;
    private AutoCompleteTextView searchView;
    private Button saveButton;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        // Initialize the search view and save button
        searchView = findViewById(R.id.place_search);
        saveButton = findViewById(R.id.save_location_button);

        // Set up map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Search button listener
        searchView.setOnEditorActionListener((v, actionId, event) -> {
            String query = searchView.getText().toString();
            searchPlace(query);
            return false;
        });

        // Save button click listener
        saveButton.setOnClickListener(v -> {
            if (currentMarker != null) {
                saveLocation(currentMarker.getPosition());
            } else {
                Toast.makeText(this, "Please select a location first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable location layer if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        // Set map properties
        mMap.setOnMapClickListener(latLng -> {
            if (currentMarker != null) {
                currentMarker.remove();
            }
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            saveButton.setEnabled(true);  // Enable save button when location is selected
        });
    }

    private void searchPlace(String query) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(query, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.clear(); // Clear previous markers
                currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Place"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                saveButton.setEnabled(true);  // Enable save button
            } else {
                Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error while searching for place", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLocation(LatLng latLng) {
        SharedPreferences sharedPreferences = getSharedPreferences("BarberShopPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("saved_location", latLng.latitude + "," + latLng.longitude);
        editor.apply();
        Toast.makeText(this, "Location saved", Toast.LENGTH_SHORT).show();
    }
}
