package com.vahagn.barber_line.Activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vahagn.barber_line.Activities.MainActivity.TopBarberShopsList;
import static com.vahagn.barber_line.Activities.MainActivity.isLogin;

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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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
import com.vahagn.barber_line.Admin.AdminActivity;
import com.vahagn.barber_line.Admin.AdminSettingsActivity;
import com.vahagn.barber_line.Classes.BarberShops;
import com.vahagn.barber_line.Fragments.SpecialistsFragment;
import com.vahagn.barber_line.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private Marker currentLocationMarker;
    private GoogleMap gMap;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    LinearLayout barbershop_infoLayout_Map;
    TextView barbershop_name_info, barbershop_address_info;
    ImageView barbershop_image_info;

    List<String> barberShopsNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        barbershop_infoLayout_Map = findViewById(R.id.barbershop_infoLayout_Map);
        barbershop_name_info = findViewById(R.id.barbershop_name_info);
        barbershop_address_info = findViewById(R.id.barbershop_address_info);
        barbershop_image_info = findViewById(R.id.barbershop_image_info);

        barbershop_infoLayout_Map.setVisibility(GONE);
        barbershop_infoLayout_Map.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), BarberShopsAboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("from_where", "MapActivity");
            getApplicationContext().startActivity(intent);
        });


        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                barberShopsNames
        );
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);

            BarberShops shop = TopBarberShopsList.stream()
                    .filter(b -> b.getName().equals(selected))
                    .findFirst()
                    .orElse(null);

            if (shop != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
                }

                barbershop_infoLayout_Map.setVisibility(VISIBLE);

                BarbersActivity.imageUrl = shop.getImage();
                BarbersActivity.name = shop.getName();
                BarbersActivity.rating = String.valueOf(shop.getRating());
                BarbersActivity.OwnerEmail = shop.getOwnerEmail();
                BarbersActivity.address = shop.getAddress();
                BarbersActivity.logo = shop.getLogo();
                BarbersActivity.coordinates = shop.getCoordinates();
                BarbersActivity.ListSpecialist = shop.getSpecialists();
                BarbersActivity.ListReviews = shop.getReviews();
                BarbersActivity.openingTimes = shop.getOpeningTimes();

                barbershop_name_info.setText(BarbersActivity.name);
                barbershop_address_info.setText(BarbersActivity.address);

                Glide.with(MapActivity.this)
                        .load(BarbersActivity.imageUrl)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(60)))
                        .into(barbershop_image_info);

                String[] coords = shop.getCoordinates().split(" ");
                try {
                    double lat = Double.parseDouble(coords[0].replace(",", "").trim());
                    double lng = Double.parseDouble(coords[1].replace(",", "").trim());
                    LatLng selectedLatLng = new LatLng(lat, lng);
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 15));
                } catch (NumberFormatException e) {
                    Log.e("MapActivity", "Invalid coordinates: " + shop.getCoordinates());
                }
            } else {
                Toast.makeText(MapActivity.this, "Barbershop not found", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setupAutoCompleteBarbershopSearch() {
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                barberShopsNames
        );
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);

            BarberShops shop = TopBarberShopsList.stream()
                    .filter(b -> b.getName().equals(selected))
                    .findFirst()
                    .orElse(null);

            if (shop != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
                }

                barbershop_infoLayout_Map.setVisibility(VISIBLE);

                BarbersActivity.imageUrl = shop.getImage();
                BarbersActivity.name = shop.getName();
                BarbersActivity.rating = String.valueOf(shop.getRating());
                BarbersActivity.OwnerEmail = shop.getOwnerEmail();
                BarbersActivity.address = shop.getAddress();
                BarbersActivity.logo = shop.getLogo();
                BarbersActivity.coordinates = shop.getCoordinates();
                BarbersActivity.ListSpecialist = shop.getSpecialists();
                BarbersActivity.ListReviews = shop.getReviews();
                BarbersActivity.openingTimes = shop.getOpeningTimes();

                barbershop_name_info.setText(BarbersActivity.name);
                barbershop_address_info.setText(BarbersActivity.address);

                Glide.with(MapActivity.this)
                        .load(BarbersActivity.imageUrl)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(60)))
                        .into(barbershop_image_info);

                String[] coords = shop.getCoordinates().split(" ");
                try {
                    double lat = Double.parseDouble(coords[0].replace(",", "").trim());
                    double lng = Double.parseDouble(coords[1].replace(",", "").trim());
                    LatLng selectedLatLng = new LatLng(lat, lng);
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 15));
                } catch (NumberFormatException e) {
                    Log.e("MapActivity", "Invalid coordinates: " + shop.getCoordinates());
                }
            } else {
                Toast.makeText(MapActivity.this, "Barbershop not found", Toast.LENGTH_SHORT).show();
            }
        });
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

                    LatLng location = new LatLng(latitude, longitude);

                    Log.i("img", barberShop.getLogo());
                    barberShopsNames.add(barberShop.getName());
                    addCustomMarker(location, barberShop.getName(), barberShop.getLogo());
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


    private void addCustomMarker(LatLng location, String Name, String logo) {
        new LoadImageTask(location, Name).execute(logo);
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final LatLng location;
        private String Name;

        public LoadImageTask(LatLng location, String Name) {
            this.location = location;
            this.Name = Name;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            String logoData = params[0];
            Bitmap originalBitmap = null;

            try {
                if (logoData != null && logoData.startsWith("data:image")) {
                    String base64String = logoData.split(",")[1];
                    byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
                    originalBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                } else if (logoData != null && !logoData.isEmpty()) {
                    originalBitmap = BitmapFactory.decodeStream(new java.net.URL(logoData).openStream());
                }

                if (originalBitmap != null) {
                    int width = 130;
                    int height = 130;
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false);
                    return addRoundedCornersAndBorder(resizedBitmap, 15, 10);
                } else {
                    originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 130, 130, false);
                    return addRoundedCornersAndBorder(resizedBitmap, 15, 10);
                }
            } catch (Exception e) {
                Log.e("imageEror", "Error loading image: " + e.getMessage());
                originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_avatar);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 130, 130, false);
                return addRoundedCornersAndBorder(resizedBitmap, 15, 10);
            }
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
                    .title(bitmap != null ? Name : "Default Marker");

            gMap.addMarker(markerOptions);
            gMap.setOnMarkerClickListener(marker -> {


                barbershop_infoLayout_Map.setVisibility(VISIBLE);

                List<BarberShops> barberShops = TopBarberShopsList.stream()
                        .filter(shop -> Objects.equals(marker.getTitle(), shop.getName()))
                        .collect(Collectors.toList());

                barberShops.forEach(shop -> {
                    BarbersActivity.imageUrl = shop.getImage();
                    BarbersActivity.name = shop.getName();
                    BarbersActivity.rating = String.valueOf(shop.getRating());
                    BarbersActivity.OwnerEmail = shop.getOwnerEmail();
                    BarbersActivity.address = shop.getAddress();
                    BarbersActivity.logo = shop.getLogo();
                    BarbersActivity.coordinates = shop.getCoordinates();
                    BarbersActivity.ListSpecialist = shop.getSpecialists();
                    BarbersActivity.ListReviews = shop.getReviews();
                    BarbersActivity.openingTimes = shop.getOpeningTimes();

                    barbershop_name_info.setText(BarbersActivity.name);
                    barbershop_address_info.setText(BarbersActivity.address);

                    Glide.with(MapActivity.this)
                            .load(BarbersActivity.imageUrl)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(60)))
                            .into(barbershop_image_info);
                });

//                        Intent intent = new Intent(getApplicationContext(), BarberShopsAboutActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


//                    List<BarberShops> barberShops = TopBarberShopsList.stream()
//                            .filter(shop -> Objects.equals(marker.getTitle(), shop.getName()))
//                            .collect(Collectors.toList());

//                barberShops.forEach(shop -> {
////                        intent.putExtra("from_where", "MapActivity");
//
//                        BarbersActivity.imageUrl = shop.getImage();
//                        BarbersActivity.name = shop.getName();
//                        BarbersActivity.rating = String.valueOf(shop.getRating());
//
//                        if (shop.getOwnerEmail()!=null)
//                        {
//                            Log.i("OwnerEmail",shop.getOwnerEmail());
//                        }
//                        else
//                            Log.i("OwnerEmail","Null");
//                        BarbersActivity.OwnerEmail = shop.getOwnerEmail();
//
//                        BarbersActivity.address = shop.getAddress();
//                        BarbersActivity.logo = shop.getLogo();
//                        BarbersActivity.coordinates = shop.getCoordinates();
//                        BarbersActivity.ListSpecialist = shop.getSpecialists();
//                        BarbersActivity.ListReviews = shop.getReviews();
//                        BarbersActivity.openingTimes = shop.getOpeningTimes();
////                            BarbersActivity.ListService = shop.getServices();
//                    });

//                    getApplicationContext().startActivity(intent);
//                    return true;
                return false;
            });
        }

        private Bitmap addRoundedCornersAndBorder(Bitmap bitmap, int cornerRadius, int borderWidth) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Bitmap newBitmap = Bitmap.createBitmap(width + 2 * borderWidth, height + 2 * borderWidth, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);

            paint.setShader(new LinearGradient(0, 0, width + 2 * borderWidth, height + 2 * borderWidth,
                    Color.DKGRAY, Color.LTGRAY, Shader.TileMode.CLAMP));
            RectF rectF = new RectF(0, 0, width + 2 * borderWidth, height + 2 * borderWidth);
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

            paint.setShader(null);
            Bitmap roundedBitmap = getRoundedBitmap(bitmap, cornerRadius);
            canvas.drawBitmap(roundedBitmap, borderWidth, borderWidth, null);
            return newBitmap;
        }

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

    public void ToAdmin(View view) {
        if (isLogin)
            navigateTo(AdminActivity.class);
        else
            navigateTo(LoginActivity.class);
    }

    public void ToBarbers(View view) {
        navigateTo(BarbersActivity.class);
    }

    public void ToBooks(View view) {
        if (isLogin)
            navigateTo(BooksActivity.class);
        else
            navigateTo(LoginActivity.class);
    }
}
