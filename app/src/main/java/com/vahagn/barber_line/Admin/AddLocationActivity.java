package com.vahagn.barber_line.Admin;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdate;
import com.vahagn.barber_line.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker currentLocationMarker;
    private GoogleMap myMap;
    private SearchView mapSearchView;

    private TextView address_textView;
    private Button Done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        address_textView = findViewById(R.id.address);
        Done = findViewById(R.id.Done);

        mapSearchView = findViewById(R.id.mapSearch);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Done.setOnClickListener(v -> Done());
    }

    private void Done() {
        if (currentLocationMarker != null) {
            LatLng position = currentLocationMarker.getPosition();
            String address = address_textView.getText().toString();

            Intent intent = new Intent(this, CreateBarberShopActivity.class);
            intent.putExtra("latitude", position.latitude);
            intent.putExtra("longitude", position.longitude);
            intent.putExtra("address", address);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    findViewById(R.id.main),
                    "sharedImageTransition");

            startActivity(intent, options.toBundle());
        } else {
            Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            updateLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        myMap.setOnCameraIdleListener(() -> {
            LatLng target = myMap.getCameraPosition().target;
            updateMarkerPosition(target);
            getAddressFromLatLng(target);
        });
    }

    private void updateLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null && myMap != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    updateMarkerPosition(currentLatLng);
                    animateCamera(currentLatLng, 15);
                }
            });
        }
    }

    private void updateMarkerPosition(LatLng latLng) {
        if (myMap != null) {
            if (currentLocationMarker != null) {
                currentLocationMarker.setPosition(latLng);
            } else {
                currentLocationMarker = myMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
            }
        }
    }

    private void animateCamera(LatLng latLng, float zoom) {
        if (myMap != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            myMap.animateCamera(cameraUpdate);
        }
    }

    private void searchLocation(String locationName) {
        if (locationName == null || locationName.isEmpty()) {
            Toast.makeText(this, "Enter the name of the place", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                Log.i("Coordinates", "Lat: " + address.getLatitude() + ", Lng: " + address.getLongitude());
                address_textView.setText(locationName);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                animateCamera(latLng, 15);
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Location search error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation();
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                StringBuilder fullAddress = new StringBuilder();

                if (address.getThoroughfare() != null) {
                    fullAddress.append(address.getThoroughfare());
                }
                if (address.getFeatureName() != null) {
                    fullAddress.append(" ").append(address.getFeatureName());
                }
                if (address.getLocality() != null) {
                    fullAddress.append(", ").append(address.getLocality());
                }

                address_textView.setText(fullAddress.toString());
                Log.i("Address", "Address: " + fullAddress.toString());
                Log.i("Coordinates", "Lat: " + latLng.latitude + ", Lng: " + latLng.longitude);
            } else {
                address_textView.setText("Address not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to get address", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.main),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }
    public void ToCreateBarberShop(View view) {
        onBackPressed();
    }
}