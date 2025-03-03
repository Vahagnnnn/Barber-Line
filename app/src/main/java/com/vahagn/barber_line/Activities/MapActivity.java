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
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
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
import com.vahagn.barber_line.Classes.Barbers;
import com.vahagn.barber_line.Classes.Services;
import com.vahagn.barber_line.R;

import java.io.Serializable;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private Marker currentLocationMarker;
    private GoogleMap gMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public String logo, image, name, address;
    public double rating;
    public List<Barbers> ListSpecialist;
    public List<Services> ListService;

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

                    image = barberShop.getImage();
                    logo = barberShop.getLogo();
                    name = barberShop.getName();
                    rating = barberShop.getRating();
                    address = barberShop.getAddress();
                    ListSpecialist = barberShop.getSpecialists();
                    ListService = barberShop.getServices();

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


    private void addCustomMarker(LatLng location, String logo) {
        new LoadImageTask(location).execute(logo);
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

                    // Apply rounded corners and add gradient border
                    Bitmap finalBitmap = addRoundedCornersAndBorder(resizedBitmap, 15, 10);
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

            // Add the marker to the map
            Marker marker = gMap.addMarker(markerOptions);

            // Set the OnClickListener for the marker
            gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Custom action when the marker is clicked
                    if (marker.equals(marker)) {
                        Toast.makeText(getApplicationContext(), "Marker clicked!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), BarberShopsAboutActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("from_where", "MapActivity");
                        intent.putExtra("image", image);
                        intent.putExtra("name", name);
                        intent.putExtra("rating", String.valueOf(rating));
                        intent.putExtra("address", address);
                        intent.putExtra("ListSpecialist", (Serializable) ListSpecialist);
                        intent.putExtra("ListService", (Serializable) ListService);

                        Log.d("IntentData1", "Image URL: " + image);
                        Log.d("IntentData1", "Name: " + name);
                        Log.d("IntentData1", "Rating: " + String.valueOf(rating));
                        Log.d("IntentData1", "Address: " + address);

                        getApplicationContext().startActivity(intent);

                        return true;
                    }
                    return false;
                }

            });
        }

        // Adding rounded corners and gradient border
        private Bitmap addRoundedCornersAndBorder(Bitmap bitmap, int cornerRadius, int borderWidth) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Bitmap newBitmap = Bitmap.createBitmap(width + 2 * borderWidth, height + 2 * borderWidth, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);  // Enable anti-aliasing for smoother edges
            paint.setColor(Color.WHITE);  // Default background for the border

            // Draw rounded rectangle with gradient border
            paint.setShader(new LinearGradient(0, 0, width + 2 * borderWidth, height + 2 * borderWidth,
                    Color.DKGRAY, Color.LTGRAY, Shader.TileMode.CLAMP));
            RectF rectF = new RectF(0, 0, width + 2 * borderWidth, height + 2 * borderWidth);
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

            // Draw image with rounded corners
            paint.setShader(null);  // Reset the shader to draw the image without gradient
            Bitmap roundedBitmap = getRoundedBitmap(bitmap, cornerRadius);
            canvas.drawBitmap(roundedBitmap, borderWidth, borderWidth, null);
            return newBitmap;
        }

        // Function to add rounded corners to the image
        private Bitmap getRoundedBitmap(Bitmap bitmap, int cornerRadius) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            return output;
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
