package com.vahagn.barber_line.Activities;

import static com.vahagn.barber_line.Activities.MainActivity.TopBarberShopsList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.R;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private Marker currentLocationMarker;
    private GoogleMap gMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            updateLocation();

            for (BarberShops barberShop : TopBarberShopsList) {
                String[] coords = barberShop.getCoordinates().split(" ");
                String latitudeString = coords[0].replace(",", "").trim();
                String longitudeString = coords[1].replace(",", "").trim();

                try {
                    double latitude = Double.parseDouble(latitudeString);
                    double longitude = Double.parseDouble(longitudeString);

                    Log.i("coords", String.valueOf(latitude));
                    Log.i("coords", String.valueOf(longitude));

                    LatLng location = new LatLng(latitude, longitude);
                    addCustomMarker(location, barberShop.getLogo());
                } catch (NumberFormatException e) {
                    Log.i("coords", "Error parsing coordinates: " + e.getMessage());
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        LatLng defaultLocation = new LatLng(40.17763162763801, 44.512459783931945);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 45));
    }

    private void updateLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new com.google.android.gms.tasks.OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                                if (currentLocationMarker != null) {
                                    currentLocationMarker.setPosition(currentLatLng);
                                } else {
                                    currentLocationMarker = gMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location"));
                                }

                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation();
            } else {
                Toast.makeText(this, "Location permission is required to show your current location on the map.", Toast.LENGTH_LONG).show();
            }
        }
    }

//    private void addCustomMarker(LatLng location, String imageUrl) {
//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(location)
//                .icon(imageUrl);
//        gMap.addMarker(markerOptions);
//    }

//    private void addCustomMarker(LatLng location, String imageUrl) {
//        String image = "https://mir-s3-cdn-cf.behance.net/projects/404/c57fb297601051.Y3JvcCw3NzgsNjA4LDQyMCwyMzU.jpg";
//            try {
//                Bitmap bitmap = BitmapFactory.decodeStream(new java.net.URL(image).openStream());
//                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(location)
//                        .icon(icon)
//                        .title("Current Location");
//                gMap.addMarker(markerOptions);
//            } catch (Exception e) {
//                Log.e("coords", "Error loading image: " + e.getMessage());
//                BitmapDescriptor defaultIcon = BitmapDescriptorFactory.defaultMarker();
//                MarkerOptions markerOptions = new MarkerOptions()
//                        .position(location)
//                        .icon(defaultIcon)
//                        .title("Default Marker");
//                gMap.addMarker(markerOptions);
//            }
//    }


    private void addCustomMarker(LatLng location, String imageUrl) {
        new LoadImageTask(location).execute(imageUrl);
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final LatLng location;

        public LoadImageTask(LatLng location) {
            this.location = location;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                Bitmap originalBitmap = BitmapFactory.decodeStream(new java.net.URL(params[0]).openStream());

                if (originalBitmap != null) {
                    int width = 130;
                    int height = 130;
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);

                    Bitmap finalBitmap = addBorderToBitmap(resizedBitmap, 5);
                    return finalBitmap;
                }
            } catch (Exception e) {
                Log.e("coords", "Error loading image: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            BitmapDescriptor icon;
            if (bitmap != null) {
                icon = BitmapDescriptorFactory.fromBitmap(bitmap);
            } else {
                icon = BitmapDescriptorFactory.defaultMarker();
            }

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .icon(icon)
                    .title(bitmap != null ? "Current Location" : "Default Marker");
            gMap.addMarker(markerOptions);
        }

        private Bitmap addBorderToBitmap(Bitmap bitmap, int borderWidth) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Bitmap newBitmap = Bitmap.createBitmap(width + 2 * borderWidth, height + 2 * borderWidth, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(newBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawRect(0, 0, newBitmap.getWidth(), newBitmap.getHeight(), paint);

            canvas.drawBitmap(bitmap, borderWidth, borderWidth, null);
            return newBitmap;
        }
    }



    private void navigateTo(Class<?> targetActivity) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                findViewById(R.id.bottom_navigation),
                "sharedImageTransition");
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent, options.toBundle());
    }

    public void ToHome(View view) {
        navigateTo(MainActivity.class);
    }

    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
    }
}
